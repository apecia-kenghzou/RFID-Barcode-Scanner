package com.uhf.util;

/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */



import java.util.HashMap;
import java.util.Map;

public class CommandArgument {

    private final Map<String, String> arguments = new HashMap<String, String>();

    private CommandArgument(String[] args) {
        String name = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i].trim();
            if (name == null) {
                if (arg.startsWith("-")) {
                    name = arg.replaceFirst("^-+", "");
                    if (name.length() < 1) {
                        name = null;
                    }
                }
                continue;
            }

            if (arg.startsWith("-")) {
                arguments.put(name.toLowerCase(), null);

                name = arg.replaceFirst("^-+", "");
                if (name.length() < 1) {
                    name = null;
                }
            } else {
                arguments.put(name.toLowerCase(), arg);
                name = null;
            }
        }

        if (name != null) {
            arguments.put(name.toLowerCase(), null);
        }
    }

    public static CommandArgument parse(String[] args) {
        return new CommandArgument(args);
    }

    public String get(String name) {
        return arguments.get(name.toLowerCase());
    }

    public String get(String name, String defaultValue) {
        String value = arguments.get(name.toLowerCase());
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public String getNotNull(String name) {
        String value = get(name);
        if (value == null) {
            throw new RuntimeException("Missing required argument for " + name);
        }
        return value;
    }

    public String getNotNull(String name, String defaultValue) {
        String value = get(name, defaultValue);
        if (value == null) {
            throw new RuntimeException("Missing required argument for " + name);
        }
        return value;
    }

}