/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.workbench.client;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Image;
import org.guvnor.common.services.shared.config.AppConfigService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.security.shared.api.identity.User;
import org.jbpm.workbench.client.i18n.Constants;
import org.jbpm.workbench.client.perspectives.ProcessAdminSettingsPerspective;
import org.jbpm.workbench.client.perspectives.ProjectAuthoringPerspective;
import org.jbpm.workbench.client.perspectives.TaskAdminSettingsPerspective;
import org.kie.workbench.common.screens.search.client.menu.SearchMenuBuilder;
import org.kie.workbench.common.services.shared.service.PlaceManagerActivityService;
import org.kie.workbench.common.workbench.client.entrypoint.DefaultWorkbenchEntryPoint;
import org.kie.workbench.common.workbench.client.menu.DefaultWorkbenchFeaturesMenusHelper;
import org.uberfire.client.mvp.AbstractWorkbenchPerspectiveActivity;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.views.pfly.menu.MainBrand;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBarPresenter;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import static org.kie.workbench.common.workbench.client.PerspectiveIds.*;

@EntryPoint
public class ShowcaseEntryPoint extends DefaultWorkbenchEntryPoint {

    protected Constants constants = Constants.INSTANCE;

    protected SyncBeanManager iocManager;

    protected User identity;

    protected DefaultWorkbenchFeaturesMenusHelper menusHelper;

    protected WorkbenchMenuBarPresenter menuBar;

    @Inject
    public ShowcaseEntryPoint(final Caller<AppConfigService> appConfigService,
                              final Caller<PlaceManagerActivityService> pmas,
                              final ActivityBeansCache activityBeansCache,
                              final SyncBeanManager iocManager,
                              final User identity,
                              final DefaultWorkbenchFeaturesMenusHelper menusHelper,
                              final WorkbenchMenuBarPresenter menuBar) {
        super(appConfigService, pmas, activityBeansCache);
        this.iocManager = iocManager;
        this.identity = identity;
        this.menusHelper = menusHelper;
        this.menuBar = menuBar;
    }

    @Override
    protected void setupMenu() {

        final AbstractWorkbenchPerspectiveActivity defaultPerspective = menusHelper.getDefaultPerspectiveActivity();
        final Menus menus = MenuFactory
                .newTopLevelMenu(constants.Home()).place(new DefaultPlaceRequest(defaultPerspective.getIdentifier())).endMenu()
                .newTopLevelMenu(constants.Authoring()).withItems(getAuthoringViews()).endMenu()
                .newTopLevelMenu(constants.Deploy()).withItems(getDeploymentViews()).endMenu()
                .newTopLevelMenu(constants.Process_Management()).withItems(getProcessManagementViews()).endMenu()
                .newTopLevelMenu(constants.Work()).withItems(getWorkViews()).endMenu()
                .newTopLevelMenu(constants.Dashboards()).withItems(getDashboardsViews()).endMenu()
                .newTopLevelMenu(constants.Extensions()).withItems(menusHelper.getExtensionsViews()).endMenu()
                .newTopLevelMenu(constants.Experimental()).withItems(getExperimentalViews()).endMenu()
                .newTopLevelCustomMenu(iocManager.lookupBean(SearchMenuBuilder.class).getInstance()).endMenu()
                .build();

        menuBar.addMenus(menus);

        menusHelper.addRolesMenuItems();
        menusHelper.addGroupsMenuItems();
        menusHelper.addWorkbenchConfigurationMenuItem();
        menusHelper.addUtilitiesMenuItems();
    }

    protected List<? extends MenuItem> getAuthoringViews() {
        final List<MenuItem> result = new ArrayList<>(1);

        result.add(MenuFactory.newSimpleItem(constants.Process_Authoring()).perspective(ProjectAuthoringPerspective.PERSPECTIVE_ID).endMenu().build().getItems().get(0));

        return result;
    }

    protected List<? extends MenuItem> getProcessManagementViews() {
        final List<MenuItem> result = new ArrayList<>(3);

        result.add(MenuFactory.newSimpleItem(constants.Process_Definitions()).perspective(PROCESS_DEFINITIONS).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Process_Instances()).perspective(PROCESS_INSTANCES).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Process_Instances_Admin()).perspective(ProcessAdminSettingsPerspective.PERSPECTIVE_ID).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Tasks_Admin()).perspective(TASKS_ADMIN).endMenu().build().getItems().get(0));

        return result;
    }

    protected List<? extends MenuItem> getExperimentalViews() {
        final List<MenuItem> result = new ArrayList<>(3);

        result.add(MenuFactory.newSimpleItem(constants.Grid_Base_Test()).perspective("Grid Base Test").endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Logs()).perspective("Logs").endMenu().build().getItems().get(0));

        return result;
    }

    protected List<? extends MenuItem> getDeploymentViews() {
        final List<MenuItem> result = new ArrayList<>(2);

        result.add(MenuFactory.newSimpleItem(constants.Execution_Servers()).place(new DefaultPlaceRequest(SERVER_MANAGEMENT)).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Jobs()).perspective(JOBS).endMenu().build().getItems().get(0));

        return result;
    }

    protected List<? extends MenuItem> getWorkViews() {
        final List<MenuItem> result = new ArrayList<>(4);

        result.add(MenuFactory.newSimpleItem(constants.Tasks_List()).perspective(TASKS).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Tasks_List_Admin()).perspective(TaskAdminSettingsPerspective.PERSPECTIVE_ID).endMenu().build().getItems().get(0));
        result.add(MenuFactory.newSimpleItem(constants.Data_Sets()).perspective(DATASET_AUTHORING).endMenu().build().getItems().get(0));

        return result;
    }

    protected List<? extends MenuItem> getDashboardsViews() {
        final List<MenuItem> result = new ArrayList<>(1);

        result.add(MenuFactory.newSimpleItem(constants.Process_Dashboard()).perspective(PROCESS_DASHBOARD).endMenu().build().getItems().get(0));

        return result;
    }

    @Produces
    @ApplicationScoped
    public MainBrand createBrandLogo() {
        return () -> new Image(AppResource.INSTANCE.images().logo());
    }
}
