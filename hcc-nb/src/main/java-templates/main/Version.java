/*
 * Copyright 2015 Alessandro Falappa <alex.falappa@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main;

/**
 * Version constants from maven build tool.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class Version {

    public static final String NUMBER = "${project.version}";
    public static final String ARTIFACT = "${project.artifactId}";
    public static final String GROUP = "${project.groupId}";
}
