/*******************************************************************************
 * Copyright (c) 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.export.utils;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.DeresolvingNamespaceProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.MembershipComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object in charge of converting an Element to a resolvable qualified name depending of its context. This class tries
 * its best to find the shortest resolvable name. Be aware that this element keeps a cache of the computation of
 * {@link Namespace#visibleMemberships(EList, boolean, boolean)}. It should be used on a static model, discard this
 * object if the model changes.
 *
 * @author Arthur Daussy
 */
public class NameDeresolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameDeresolver.class);

    /**
     * Keep a cache of the visible memberships of a given Namespace identified by its elementId.
     */
    private Map<String, EList<Membership>> visibleMembershipsCache = new HashMap<>();

    /**
     * Keep a cache the deresolved name of an element from a given namespace. The key of the map is a pair of the
     * elementId of the deresolving namespace and the element itself.
     */
    private Map<Pair<String, String>, String> deresolvedNamesCache = new HashMap<>();

    /**
     * Keeps a cache of the qualified name of an element identified by its elementId.
     */
    private Map<String, String> qualifiedNameCache = new HashMap<>();
    
    private DeresolvingNamespaceProvider deresolvingNamespaceProvider = new DeresolvingNamespaceProvider();

    public String getDeresolvedName(Element element, Element context) {

        if (element == null) {
            return null;
        }

        Namespace deresolvingNamespace = deresolvingNamespaceProvider.getDeresolvingNamespace(context);

        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = getQualifiedName(element);
        } else {
            Set<Membership> elementAncestors = EMFUtils.getAncestors(Membership.class, element, null).stream().collect(toSet());
            qualifiedName = this.deresolve(element, deresolvingNamespace, deresolvingNamespace, elementAncestors);
        }

        return qualifiedName;

    }

    private String getQualifiedName(Element e) {
        if (e == null) {
            return "";
        }
        String qn = qualifiedNameCache.get(e.getElementId());
        if (qn == null) {
            qn = e.getQualifiedName();
            if (qn == null) {
                qn = "";
            }
            qualifiedNameCache.put(e.getElementId(), qn);
        }
        return qn;
    }

    /**
     * Deresolve the name of the given element.
     *
     * @param element
     *            the element to deresolve.
     * @param sourceNamespace
     *            the original owning namespace of the element
     * @param deresolvingNamespace
     *            the current namespace used to deresolved the name of the element
     * @param ancestors
     *            the ancestor memberships of the element from which it can be reached
     * @return a name
     */
    private String deresolve(Element element, Namespace sourceNamespace, Namespace deresolvingNamespace, Set<Membership> ancestors) {
        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = getQualifiedName(element);
        } else {
            Pair<String, String> searchKey = Pair.of(deresolvingNamespace.getElementId(), element.getElementId());
            String cacheValue = deresolvedNamesCache.get(searchKey);
            if (cacheValue != null) {
                return cacheValue;
            }
            EList<Membership> visibleMemberships = this.getVisibleMemberships(deresolvingNamespace, deresolvingNamespace == sourceNamespace);
            final Stream<Membership> stream;
            // Some element have a lots of elements to checks. In that case use parallel stream
            if (visibleMemberships.size() > 100) {
                stream = visibleMemberships.parallelStream();
            } else {
                stream = visibleMemberships.stream();
            }
            Optional<Membership> importedContainer = stream.filter(ancestors::contains)
                    // Get the membership the closest to the element to deresolve
                    .sorted(Comparator.comparing(m -> this.getPathLenght(element, m))).findFirst();

            if (!importedContainer.isEmpty()) {
                // We found a visible membership that can reach the element
                // Try to compute its qualified name
                qualifiedName = this.buildRelativeQualifiedName(element, deresolvingNamespace, importedContainer.get(), sourceNamespace);
            } else {
                // Ask to the parent namespace
                qualifiedName = this.deresolve(element, sourceNamespace, deresolvingNamespace.getOwningNamespace(), ancestors);
            }

            if (qualifiedName != null) {
                deresolvedNamesCache.put(searchKey, qualifiedName);
            }
        }

        return qualifiedName;
    }

    private int getPathLenght(Element element, Membership ancestor) {
        int lenght = 0;
        EObject current = element;
        while (current != null && ancestor != current) {
            lenght++;
            current = current.eContainer();
        }
        return lenght;
    }

    private EList<Membership> getVisibleMemberships(Namespace deresolvingNamespace, boolean includePrivate) {
        EList<Membership> memberships = null;
        String namespaceId = deresolvingNamespace.getElementId();
        if (namespaceId != null) {
            memberships = this.visibleMembershipsCache.get(namespaceId);
        }
        if (memberships == null) {
            EList<Membership> visible = new MembershipComputer<Namespace>(deresolvingNamespace, new UniqueEList<>()).visibleMemberships(false, includePrivate, true);
            memberships = visible.stream()
                    .filter(m -> m.getMemberElement() != null)
                    .filter(m -> includePrivate || m.getVisibility() != VisibilityKind.PRIVATE)
                    .collect(toCollection(UniqueEList<Membership>::new));
            this.visibleMembershipsCache.put(namespaceId, memberships);
        }
        return memberships;
    }

    private String buildRelativeQualifiedName(Element element, Namespace owningNamespace, Membership visibleMembership, Namespace sourceNamespace) {
        String elementQn = getQualifiedName(element);
        if (elementQn == null || elementQn.isEmpty()) {
            LOGGER.warn("No qualified name found for " + element.getElementId());
            return "";
        }

        // Qualified name between visible membership and the element
        String relativeQualifiedName = this.getRelativeQualifiedName(elementQn, element, visibleMembership);

        Membership resolvedElement = sourceNamespace.resolve(relativeQualifiedName);
        // If the name resolve against an element which is not the expected element it means that there is a name
        // conflict.
        // In that case keep we need a more detailed qualified name
        if (resolvedElement != null && resolvedElement != element.getOwningMembership()) {
            // Last try if the element is in the containment tree find the shortest qualified name
            String qualifiedName = getQualifiedName(owningNamespace);
            if (qualifiedName != null && !qualifiedName.isBlank() && elementQn.startsWith(qualifiedName)) {
                relativeQualifiedName = elementQn.substring(qualifiedName.length() + 2, elementQn.length());
            } else {
                relativeQualifiedName = elementQn;
            }
        }

        return relativeQualifiedName;

    }

    private String getRelativeQualifiedName(String elementQn, Element element, Membership m) {
        final String qn;
        Element importedElement = m.getMemberElement();
        String importedElementQualifiedName = getQualifiedName(importedElement);
        if (importedElement != element && importedElementQualifiedName != null && !importedElementQualifiedName.isEmpty()) {
            int partToRemove = importedElementQualifiedName.length() + 2;
            qn = Appender.toPrintableName(importedElement.getName()) + "::" + elementQn.substring(partToRemove, elementQn.length());
        } else {
            qn = Appender.toPrintableName(element.getName());
        }
        return qn;
    }


}
