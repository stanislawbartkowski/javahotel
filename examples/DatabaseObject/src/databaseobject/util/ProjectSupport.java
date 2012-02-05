/*
 * Copyright 2012 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package databaseobject.util;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import databaseobject.messages.Messages;

/**
 * Utilities (static) related to project creation
 * 
 * @author sbartkowski
 * 
 */
public class ProjectSupport {

	/**
	 * Creates project with name and location
	 * 
	 * @param projectName
	 *            project name
	 * @param location
	 *            location
	 * @return IProject just created
	 */
	public static IProject createProject(String projectName, URI location) {

		IProject project = null;
		try {
			project = createBaseProject(projectName, location);
			addNature(project);

			String[] paths = { CommonProp.PLSQLPACKAGE };

			addToProjectStructure(project, paths);
		} catch (CoreException e) {
			LogUtil.log(Messages.ProjectSupport_0, e);
			project = null;
		}

		return project;
	}

	private static IProject createBaseProject(String projectName, URI location)
			throws CoreException {

		IProject newProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace()
					.newProjectDescription(newProject.getName());
			if (location != null
					&& ResourcesPlugin.getWorkspace().getRoot()
							.getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);
			newProject.create(desc, null);
			if (!newProject.isOpen()) {
				newProject.open(null);
			}
		}

		return newProject;
	}

	private static void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * Create folder in project space
	 * @param newProject IProject
	 * @param paths List of folder to create
	 * @throws CoreException
	 */
	public static void createFolder(IProject newProject, String[] paths)
			throws CoreException {
		String s = null;
		for (String path : paths) {
			if (s == null) {
				s = path;
			} else {
				s += "/" + path;} //$NON-NLS-1$
			IFolder i = newProject.getFolder(s);
			createFolder(i);
		}
	}

	private static void addToProjectStructure(IProject newProject,
			String[] paths) throws CoreException {
		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

	private static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(CommonProp.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = CommonProp.NATURE_ID;
			description.setNatureIds(newNatures);

			IProgressMonitor monitor = null;
			project.setDescription(description, monitor);
		}
	}

}