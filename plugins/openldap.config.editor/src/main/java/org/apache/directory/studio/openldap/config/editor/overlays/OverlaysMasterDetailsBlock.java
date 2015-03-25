package org.apache.directory.studio.openldap.config.editor.overlays;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import org.apache.directory.studio.openldap.config.OpenLdapConfigurationPlugin;
import org.apache.directory.studio.openldap.config.OpenLdapConfigurationPluginConstants;
import org.apache.directory.studio.openldap.config.model.OlcAccessLogConfig;
import org.apache.directory.studio.openldap.config.model.OlcAuditlogConfig;
import org.apache.directory.studio.openldap.config.model.OlcChainConfig;
import org.apache.directory.studio.openldap.config.model.OlcConfig;
import org.apache.directory.studio.openldap.config.model.OlcDistProcConfig;
import org.apache.directory.studio.openldap.config.model.OlcOverlayConfig;
import org.apache.directory.studio.openldap.config.model.OlcPBindConfig;
import org.apache.directory.studio.openldap.config.model.OlcSyncProvConfig;
import org.apache.directory.studio.openldap.config.model.OpenLdapConfiguration;


/**
 * This class represents the Overlays Master/Details Block used in the Overlays Page.
 */
public class OverlaysMasterDetailsBlock extends MasterDetailsBlock
{
    /** The associated page */
    private OverlaysPage page;

    // UI Fields
    private TableViewer viewer;
    private Button addButton;
    private Button deleteButton;


    /**
     * Creates a new instance of OverlaysMasterDetailsBlock.
     *
     * @param page
     *      the associated page
     */
    public OverlaysMasterDetailsBlock( OverlaysPage page )
    {
        super();
        this.page = page;
    }


    /**
     * {@inheritDoc}
     */
    public void createContent( IManagedForm managedForm )
    {
        super.createContent( managedForm );

        // Giving the weights of both parts of the SashForm.
        sashForm.setWeights( new int[]
            { 1, 2 } );
    }


    /**
     * {@inheritDoc}
     */
    protected void createMasterPart( final IManagedForm managedForm, Composite parent )
    {
        FormToolkit toolkit = managedForm.getToolkit();

        // Creating the Section
        Section section = toolkit.createSection( parent, Section.TITLE_BAR );
        section.setText( "All Overlays" );
        section.marginWidth = 10;
        section.marginHeight = 5;
        Composite client = toolkit.createComposite( section, SWT.WRAP );
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 2;
        layout.marginHeight = 2;
        client.setLayout( layout );
        toolkit.paintBordersFor( client );
        section.setClient( client );

        // Creating the Table and Table Viewer
        Table table = toolkit.createTable( client, SWT.NULL );
        GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true, 1, 2 );
        gd.heightHint = 20;
        gd.widthHint = 100;
        table.setLayoutData( gd );
        final SectionPart spart = new SectionPart( section );
        managedForm.addPart( spart );
        viewer = new TableViewer( table );
        viewer.addSelectionChangedListener( new ISelectionChangedListener()
        {
            public void selectionChanged( SelectionChangedEvent event )
            {
                managedForm.fireSelectionChanged( spart, event.getSelection() );
            }
        } );
        viewer.setContentProvider( new ArrayContentProvider() );
        viewer.setLabelProvider( new LabelProvider()
        {
            public String getText( Object element )
            {
                if ( element instanceof OlcOverlayConfig )
                {
                    OlcOverlayConfig overlay = ( OlcOverlayConfig ) element;

                    return overlay.getOlcOverlay();
                }

                return super.getText( element );
            };


            public Image getImage( Object element )
            {
                if ( element instanceof OlcOverlayConfig )
                {
                    return OpenLdapConfigurationPlugin.getDefault().getImage(
                        OpenLdapConfigurationPluginConstants.IMG_OVERLAY );
                }

                return super.getImage( element );
            };
        } );

        // Creating the button(s)
        addButton = toolkit.createButton( client, "Add", SWT.PUSH );
        addButton.setLayoutData( new GridData( SWT.FILL, SWT.BEGINNING, false, false ) );

        deleteButton = toolkit.createButton( client, "Delete", SWT.PUSH );
        deleteButton.setEnabled( false );
        deleteButton.setLayoutData( new GridData( SWT.FILL, SWT.BEGINNING, false, false ) );

        initFromInput();
    }


    /**
     * Initializes the page with the Editor input.
     */
    private void initFromInput()
    {
        OpenLdapConfiguration configuration = page.getConfiguration();

        List<OlcConfig> configurationElements = configuration.getConfigurationElements();
        List<OlcOverlayConfig> overlayConfigurationElements = new ArrayList<OlcOverlayConfig>();
        for ( OlcConfig configurationElement : configurationElements )
        {
            if ( configurationElement instanceof OlcOverlayConfig )
            {
                overlayConfigurationElements.add( ( OlcOverlayConfig ) configurationElement );
            }
        }

        viewer.setInput( overlayConfigurationElements.toArray( new OlcOverlayConfig[0] ) );
    }


    /**
     * {@inheritDoc}
     */
    protected void registerPages( DetailsPart detailsPart )
    {
        AccessLogOverlayDetailsPage olcAccessLogOverlayDetailsPage = new AccessLogOverlayDetailsPage( this );
        detailsPart.registerPage( OlcAccessLogConfig.class, olcAccessLogOverlayDetailsPage );

        AuditLogOverlayDetailsPage olcAuditLogOverlayDetailsPage = new AuditLogOverlayDetailsPage( this );
        detailsPart.registerPage( OlcAuditlogConfig.class, olcAuditLogOverlayDetailsPage );

        ChainOverlayDetailsPage olcChainOverlayDetailsPage = new ChainOverlayDetailsPage( this );
        detailsPart.registerPage( OlcChainConfig.class, olcChainOverlayDetailsPage );

        DistProcOverlayDetailsPage oldDistProcOverlayDetailsPage = new DistProcOverlayDetailsPage( this );
        detailsPart.registerPage( OlcDistProcConfig.class, oldDistProcOverlayDetailsPage );

        PasswordPolicyOverlayDetailsPage olcPasswordPolicyOverlayDetailsPage = new PasswordPolicyOverlayDetailsPage(
            this );
        detailsPart.registerPage( OlcDistProcConfig.class, olcPasswordPolicyOverlayDetailsPage );

        PBindAccessOverlayDetailsPage olcPBindAccessOverlayDetailsPage = new PBindAccessOverlayDetailsPage( this );
        detailsPart.registerPage( OlcPBindConfig.class, olcPBindAccessOverlayDetailsPage );

        SyncProvOverlayDetailsPage olcSyncProvOverlayDetailsPage = new SyncProvOverlayDetailsPage( this );
        detailsPart.registerPage( OlcSyncProvConfig.class, olcSyncProvOverlayDetailsPage );
    }


    /**
     * {@inheritDoc}
     */
    protected void createToolBarActions( IManagedForm managedForm )
    {
        // No toolbar actions
    }
}
