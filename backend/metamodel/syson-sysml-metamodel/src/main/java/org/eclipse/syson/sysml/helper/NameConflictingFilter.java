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
package org.eclipse.syson.sysml.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;

/**
 * Filter in charge of checking if element has a conflicting name or short name with a set a previously checked
 * elements.
 * 
 * <p>
 * This filter only checks for elements that has at least an {@link Element#effectiveName()} or an
 * {@link Element#effectiveShortName()}. If an element has neither, it matches the given element since it can not create
 * any name conflict.
 * </p>
 * 
 * @author Arthur Daussy
 */
public class NameConflictingFilter implements Predicate<Membership> {

    private Set<String> usedNames = new HashSet<>();

    @Override
    public boolean test(Membership member) {
        if (member != null) {
            Element memberElement = member.getMemberElement();
            if (memberElement != null) {
                return checkConflictingNames(memberElement);
            }
        }

        return true;
    }

    public boolean checkConflictingNames(Element memberElement) {
        String name = memberElement.effectiveName();
        String shortName = memberElement.effectiveShortName();

        boolean hasName = name != null;
        boolean hasShortName = shortName != null;
        if (hasName || hasShortName) {
            boolean hasConflictingName = hasName && usedNames.contains(name);
            boolean hasConflictingShortName = hasShortName && usedNames.contains(shortName);

            boolean noConflict = !hasConflictingName && !hasConflictingShortName;
            if (noConflict) {
                // Only add to forbidden names if the element is not conflicting on both the shortname and the
                // name
                usedNames.add(name);
                usedNames.add(shortName);
            }
            return noConflict;
        } // Else if the element has neither a name or a short name it can not create name conflict
        return true;
    }

    public void fillUsedNames(List<Membership> existingMembers) {
        for (Membership member : existingMembers) {
            Element memberElement = member.getMemberElement();
            if (memberElement != null) {
                String name = memberElement.effectiveName();
                String shortName = memberElement.effectiveShortName();
                if (name != null) {
                    usedNames.add(name);
                }
                if (shortName != null) {
                    usedNames.add(shortName);
                }
            }
        }
    }

}
