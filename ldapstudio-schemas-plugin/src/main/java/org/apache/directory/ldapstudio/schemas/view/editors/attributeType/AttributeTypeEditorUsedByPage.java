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

package org.apache.directory.ldapstudio.schemas.view.editors.attributeType;


import org.apache.directory.ldapstudio.schemas.model.AttributeType;
import org.apache.directory.ldapstudio.schemas.model.LDAPModelEvent;
import org.apache.directory.ldapstudio.schemas.model.ObjectClass;
import org.apache.directory.ldapstudio.schemas.model.PoolListener;
import org.apache.directory.ldapstudio.schemas.model.SchemaPool;
import org.apache.directory.ldapstudio.schemas.view.editors.objectClass.ObjectClassEditor;
import org.apache.directory.ldapstudio.schemas.view.editors.objectClass.ObjectClassEditorInput;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;


/**
 * This class is the Used By Page of the Attribute Type Editor
 */
public class AttributeTypeEditorUsedByPage extends FormPage implements PoolListener
{
    /** The page ID */
    public static final String ID = AttributeTypeEditor.ID + "usedByPage";

    /** The page title */
    public static String TITLE = "Used By";

    /** The modified attribute type */
    private AttributeType modifiedAttributeType;

    /** The original attribute type */
    private AttributeType originalAttributeType;

    /** The Schema Pool */
    private SchemaPool schemaPool;

    // UI Widgets
    private Table mandatoryAttributeTable;
    private TableViewer mandatoryAttributeTableViewer;
    private Table optionalAttibuteTable;
    private TableViewer optionalAttibuteTableViewer;

    // Listeners
    /** The listener of the Mandatory Attribute Type Table*/
    private MouseAdapter mandatoryAttributeTableListener = new MouseAdapter()
    {
        public void mouseDoubleClick( MouseEvent e )
        {
            Object selectedItem = ( ( StructuredSelection ) mandatoryAttributeTableViewer.getSelection() )
                .getFirstElement();

            if ( selectedItem instanceof ObjectClass )
            {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try
                {
                    page.openEditor( new ObjectClassEditorInput( ( ObjectClass ) selectedItem ), ObjectClassEditor.ID );
                }
                catch ( PartInitException exception )
                {
                    Logger.getLogger( AttributeTypeEditorUsedByPage.class ).debug( "error when opening the editor" ); //$NON-NLS-1$
                }
            }
        }
    };

    /** The listener of the Optional Attribute Type Table*/
    private MouseAdapter optionalAttibuteTableListener = new MouseAdapter()
    {
        public void mouseDoubleClick( MouseEvent e )
        {
            Object selectedItem = ( ( StructuredSelection ) optionalAttibuteTableViewer.getSelection() )
                .getFirstElement();

            if ( selectedItem instanceof ObjectClass )
            {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try
                {
                    page.openEditor( new ObjectClassEditorInput( ( ObjectClass ) selectedItem ), ObjectClassEditor.ID );
                }
                catch ( PartInitException exception )
                {
                    Logger.getLogger( AttributeTypeEditorUsedByPage.class ).debug( "error when opening the editor" ); //$NON-NLS-1$
                }
            }
        }
    };


    /**
     * Default constructor.
     * 
     * @param editor
     *      the associated editor
     */
    public AttributeTypeEditorUsedByPage( FormEditor editor )
    {
        super( editor, ID, TITLE );
        schemaPool = SchemaPool.getInstance();
        schemaPool.addListener( this );
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent( IManagedForm managedForm )
    {
        // Getting the modified and original attribute types
        modifiedAttributeType = ( ( AttributeTypeEditor ) getEditor() ).getModifiedAttributeType();
        originalAttributeType = ( ( AttributeTypeEditor ) getEditor() ).getOriginalAttributeType();

        // Creating the base UI
        ScrolledForm form = managedForm.getForm();
        FormToolkit toolkit = managedForm.getToolkit();
        GridLayout layout = new GridLayout( 2, true );
        form.getBody().setLayout( layout );

        // As Mandatory Attribute Section
        createAsMandatoryAttributeSection( form.getBody(), toolkit );

        // As Optional Attribute Section
        createAsOptionalAttributeSection( form.getBody(), toolkit );

        // Filling the UI with values from the attribute type
        fillInUiFields();

        // Listeners initialization
        addListeners();
    }


    /**
     * Creates the As Mandatory Attribute Section.
     *
     * @param parent
     *      the parent composite
     * @param toolkit
     *      the FormToolKit to use
     */
    private void createAsMandatoryAttributeSection( Composite parent, FormToolkit toolkit )
    {
        // As Mandatory Attribute Section
        Section mandatoryAttributeSection = toolkit.createSection( parent, Section.DESCRIPTION | Section.EXPANDED
            | Section.TITLE_BAR );
        mandatoryAttributeSection.setDescription( "The attribute type '" + modifiedAttributeType.getNames()[0]
            + "' is used as a mandatory attribute in the following object classes." );
        mandatoryAttributeSection.setText( "As Mandatory Attribute" );

        // Creating the layout of the section
        Composite mandatoryAttributeSectionClient = toolkit.createComposite( mandatoryAttributeSection );
        mandatoryAttributeSectionClient.setLayout( new GridLayout() );
        toolkit.paintBordersFor( mandatoryAttributeSectionClient );
        mandatoryAttributeSection.setClient( mandatoryAttributeSectionClient );
        mandatoryAttributeSection.setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, true ) );

        mandatoryAttributeTable = toolkit.createTable( mandatoryAttributeSectionClient, SWT.NONE );
        GridData gridData = new GridData( GridData.FILL, GridData.FILL, true, true );
        gridData.heightHint = 1;
        mandatoryAttributeTable.setLayoutData( gridData );
        mandatoryAttributeTableViewer = new TableViewer( mandatoryAttributeTable );
        mandatoryAttributeTableViewer.setContentProvider( new ATEUsedByMandatoryTableContentProvider() );
        mandatoryAttributeTableViewer.setLabelProvider( new ATEUsedByTablesLabelProvider() );
    }


    /**
     * Creates the As Optional Attribute Section.
     *
     * @param parent
     *      the parent composite
     * @param toolkit
     *      the FormToolKit to use
     */
    private void createAsOptionalAttributeSection( Composite parent, FormToolkit toolkit )
    {
        // Matching Rules Section
        Section optionalAttributeSection = toolkit.createSection( parent, Section.DESCRIPTION | Section.EXPANDED
            | Section.TITLE_BAR );
        optionalAttributeSection.setDescription( "The attribute type '" + modifiedAttributeType.getNames()[0]
            + "' is used as an optional attribute in the following object classes." ); //$NON-NLS-1$
        optionalAttributeSection.setText( "As Optional Attribute" ); //$NON-NLS-1$

        // Creating the layout of the section
        Composite optionalAttributeSectionClient = toolkit.createComposite( optionalAttributeSection );
        optionalAttributeSectionClient.setLayout( new GridLayout() );
        toolkit.paintBordersFor( optionalAttributeSectionClient );
        optionalAttributeSection.setClient( optionalAttributeSectionClient );
        optionalAttributeSection.setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, true ) );

        optionalAttibuteTable = toolkit.createTable( optionalAttributeSectionClient, SWT.NONE );
        GridData gridData = new GridData( GridData.FILL, GridData.FILL, true, true );
        gridData.heightHint = 1;
        optionalAttibuteTable.setLayoutData( gridData );
        optionalAttibuteTableViewer = new TableViewer( optionalAttibuteTable );
        optionalAttibuteTableViewer.setContentProvider( new ATEUsedByOptionalTableContentProvider() );
        optionalAttibuteTableViewer.setLabelProvider( new ATEUsedByTablesLabelProvider() );
    }


    /**
     * Fills in the User Iterface.
     */
    private void fillInUiFields()
    {
        mandatoryAttributeTableViewer.setInput( originalAttributeType );
        optionalAttibuteTableViewer.setInput( originalAttributeType );
    }


    /**
     * Adds listeners to UI fields
     */
    private void addListeners()
    {
        mandatoryAttributeTable.addMouseListener( mandatoryAttributeTableListener );
        optionalAttibuteTable.addMouseListener( optionalAttibuteTableListener );
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.schemas.model.PoolListener#poolChanged(org.apache.directory.ldapstudio.schemas.model.SchemaPool, org.apache.directory.ldapstudio.schemas.model.LDAPModelEvent)
     */
    public void poolChanged( SchemaPool p, LDAPModelEvent e )
    {
        mandatoryAttributeTableViewer.refresh();
        optionalAttibuteTableViewer.refresh();
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormPage#dispose()
     */
    public void dispose()
    {
        schemaPool.removeListener( this );

        super.dispose();
    }
}
