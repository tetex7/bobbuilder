/*
 * Copyright (C) 2025  Tetex7
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.trs.bobbuilder;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.JavaCompile;

public class BobBuilderPlugin implements Plugin<Project>
{
    @Override
    public void apply(Project project) {
        /*project.getPlugins().apply(JavaPlugin.class);

        project.afterEvaluate(p -> {
            project.getTasks().withType(JavaCompile.class).configureEach(compileTask -> {
                compileTask.getOptions().getCompilerArgs().add("-processor");
                compileTask.getOptions().getCompilerArgs().add("com.mydomain.processor.MyProcessor");
                compileTask.getOptions().getAnnotationProcessorPath().plus(
                        project.files("libs/my-annotation-processor.jar")
                );
            });
        });*/
    }
}
