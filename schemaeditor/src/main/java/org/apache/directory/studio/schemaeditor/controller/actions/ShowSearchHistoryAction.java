/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.studio.schemaeditor.controller.actions;


import org.apache.directory.studio.schemaeditor.Activator;
import org.apache.directory.studio.schemaeditor.PluginConstants;
import org.apache.directory.studio.schemaeditor.view.dialogs.PreviousSearchesDialog;
import org.apache.directory.studio.schemaeditor.view.search.SearchPage;
import org.apache.directory.studio.schemaeditor.view.search.SearchPage.SearchInEnum;
import org.apache.directory.studio.schemaeditor.view.views.SearchView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * This action is show the search History.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ShowSearchHistoryAction extends Action implements IWorkbenchWindowActionDelegate
{
    /** The associated view */
    private SearchView view;


    /**
     * Creates a new instance of ShowSearchFieldAction.
     */
    public ShowSearchHistoryAction( SearchView view )
    {
        super( Messages.getString( "ShowSearchHistoryAction.SearchHistoryAction" ), AS_DROP_DOWN_MENU ); //$NON-NLS-1$
        this.view = view;
        setToolTipText( getText() );
        setImageDescriptor( Activator.getDefault().getImageDescriptor( PluginConstants.IMG_SHOW_SEARCH_HISTORY ) );
        setEnabled( true );
        setMenuCreator( new MenuCreator( view ) );
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        PreviousSearchesDialog dialog = new PreviousSearchesDialog( view );
        dialog.open();
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run( IAction action )
    {
        run();
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose()
    {
        // Nothing to do
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init( IWorkbenchWindow window )
    {
        // Nothing to do
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged( IAction action, ISelection selection )
    {
        // Nothing to do
    }
}

class MenuCreator implements IMenuCreator
{
    /** The menu */
    private Menu menu;

    /** The associated view */
    private SearchView view;


    /**
     * Creates a new instance of MenuCreator.
     *
     * @param view
     *      the associated view
     */
    public MenuCreator( SearchView view )
    {
        this.view = view;
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuCreator#dispose()
     */
    public void dispose()
    {
        if ( menu != null )
        {
            menu.dispose();
            menu = null;
        }
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
     */
    public Menu getMenu( Control parent )
    {
        menu = new Menu( parent );

        // Previous searches 
        String[] previousSearches = SearchPage.loadSearchStringHistory();
        for ( final String search : previousSearches )
        {
            MenuItem item = new MenuItem( menu, SWT.RADIO );
            item.setText( search );
            item.setImage( Activator.getDefault().getImage( PluginConstants.IMG_SEARCH_HISTORY_ITEM ) );
            item.addSelectionListener( new SelectionAdapter()
            {
                public void widgetSelected( SelectionEvent e )
                {
                    view.setSearchInput( search, SearchPage.loadSearchIn().toArray( new SearchInEnum[0] ), SearchPage
                        .loadScope() );
                }
            } );
            if ( search.equals( view.getSearchString() ) )
            {
                item.setSelection( true );
            }
        }

        // No search history
        if ( previousSearches.length == 0 )
        {
            MenuItem item = new MenuItem( menu, SWT.RADIO );
            item.setText( Messages.getString( "ShowSearchHistoryAction.None" ) ); //$NON-NLS-1$
            item.setEnabled( false );
            item.setSelection( true );
        }

        // Menu Separator
        new MenuItem( menu, SWT.SEPARATOR );

        MenuItem item = new MenuItem( menu, SWT.PUSH );
        item.setText( Messages.getString( "ShowSearchHistoryAction.History" ) ); //$NON-NLS-1$
        item.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                PreviousSearchesDialog dialog = new PreviousSearchesDialog( view );
                dialog.open();
            }
        } );
        item = new MenuItem( menu, SWT.PUSH );
        item.setText( Messages.getString( "ShowSearchHistoryAction.ClearHistory" ) ); //$NON-NLS-1$
        item.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                SearchPage.clearSearchHistory();
            }
        } );

        return menu;
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
     */
    public Menu getMenu( Menu parent )
    {
        return null;
    }
}