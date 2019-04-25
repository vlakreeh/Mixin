/*
 * This file is part of Mixin, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.asm.util;

import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small helper to resolve the current java version
 */
public abstract class JavaVersion {
    private static final Pattern patternOld = Pattern.compile("^1\\.([0-9]+)");
    private static final Pattern patternNew = Pattern.compile("^([0-9]+)");
    private static int current = -1;
    private static boolean warned = false;
    
    private JavaVersion() {}

    public static void warnUnrecognized(Logger logger) {
        if (!warned) {
            logger.warn("Could not recognize Java version: " + System.getProperty("java.version"));
            warned = true;
        }
    }

    /**
     * Get the current java version, calculates if necessary
     */
    public static int current() {
        if (JavaVersion.current < 0) {
            JavaVersion.current = JavaVersion.resolveCurrentVersion();
        }
        return JavaVersion.current;
    }

    private static int resolveCurrentVersion() {
        String version = System.getProperty("java.version");

        Matcher matcher = patternNew.matcher(version);
        if (matcher.find()) {
            current = Integer.parseInt(matcher.group(1));
            if (current > 1) {
                return current;
            } else {
                matcher = patternOld.matcher(version);
                if (matcher.find()) {
                    current = Integer.parseInt(matcher.group(1));
                    if (current >= 1) {
                        return current;
                    }
                }
            }
        }

        return 0; // fallback
    }

}
