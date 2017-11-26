package de.kapuzenholunder.germanwings.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.joda.time.DateTime;

import com.toedter.calendar.JDateChooser;

import de.kapuzenholunder.germanwings.analyzer.Searcher;
import de.kapuzenholunder.germanwings.config.ConfigurationProvider;
import de.kapuzenholunder.germanwings.data.location.CategoryInfo;
import de.kapuzenholunder.germanwings.data.location.City;
import de.kapuzenholunder.germanwings.data.location.Departure;
import de.kapuzenholunder.germanwings.data.time.DateInformation;
import de.kapuzenholunder.germanwings.data.time.Time;
import de.kapuzenholunder.germanwings.data.time.TimeRange;
import de.kapuzenholunder.germanwings.helper.IOperationFinishedCallBack;
import de.kapuzenholunder.germanwings.helper.WingsLogger;
import de.kapuzenholunder.germanwings.view.events.ISearchListener;
import de.kapuzenholunder.germanwings.view.events.SearchResult;
import de.kapuzenholunder.germanwings.view.events.SearcherEvent;

import javax.swing.JEditorPane;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;

import java.util.Calendar;

import javax.swing.border.MatteBorder;

public class MainWindow implements ISearchListener, IOperationFinishedCallBack
{
	private JFrame frmProjektBlindflug;
	private JButton startSearchButton;
	private JButton renewConfigButton;
	private JDateChooser returnDateChooser;
	private JDateChooser outwardDateChooser;
	private JComboBox<Integer> personCountComboBox;
	private JComboBox<Integer> childrenCountComboBox;
	private JComboBox<Integer> tryCountComboBox;
	private JComboBox<City> cityComboBox;
	private JComboBox<Departure> departureComboBox;
	private JComboBox<Integer> infantCountComboBox;
	private WingsLogger logger;
	private GermanWingsNavigator navigator;
	private Searcher searcher;
	
	private SpinnerDateModel outWardFromModel;
	private SpinnerDateModel outWardToModel;
	private SpinnerDateModel returnFromModel;
	private SpinnerDateModel returnToModel;

	private JSpinner outWardFromTimeSpinner;
	private JSpinner outWardToTimeSpinner;
	private JSpinner returnFromTimeSpinner;
	private JSpinner returnToTimeSpinner;

	private void commitSpinners() throws ParseException
	{
		outWardFromTimeSpinner.commitEdit();
		outWardToTimeSpinner.commitEdit();
		returnFromTimeSpinner.commitEdit();
		returnToTimeSpinner.commitEdit();
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
		this.frmProjektBlindflug.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	private void initialize()
	{
		this.frmProjektBlindflug = new JFrame();
		frmProjektBlindflug.setTitle("Projekt Blindflug 1.4");
		frmProjektBlindflug.setResizable(false);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();

		int width = 413;
		double screenWidth = dim.getWidth();
		int xPostion = 1025;

		if ((xPostion + width) > screenWidth)
		{
			xPostion = (int) screenWidth - width;
		}

		this.frmProjektBlindflug.setBounds(xPostion, 200, 376, 574);
		this.frmProjektBlindflug.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.startSearchButton = new JButton("Suche starten");
		this.startSearchButton.setBounds(251, 311, 109, 29);

		this.renewConfigButton = new JButton("Aktualisieren");
		this.renewConfigButton.setBounds(18, 311, 109, 29);

		Date today = new Date();
		Date tomorrow = new Date();
		Date dayAfterTomorrow = new Date();
		tomorrow.setDate(today.getDate() + 1);
		dayAfterTomorrow.setDate(tomorrow.getDate() + 1);

		Date maxDate = new Date();
		int maxDateINt = maxDate.getDate() + DateInformation.MaxAdvancedDays;
		maxDate.setDate(maxDateINt);

		cityComboBox = new JComboBox<City>();
		cityComboBox.setBounds(217, 44, 143, 23);

		JLabel lblZiel = new JLabel("Ziel");
		lblZiel.setBounds(18, 43, 194, 24);
		frmProjektBlindflug.getContentPane().setLayout(null);

		this.personCountComboBox = new JComboBox<Integer>();
		personCountComboBox.setBounds(217, 71, 143, 23);
		personCountComboBox.addItem(1);
		personCountComboBox.addItem(2);
		personCountComboBox.addItem(3);
		personCountComboBox.addItem(4);
		personCountComboBox.addItem(5);
		personCountComboBox.addItem(6);
		personCountComboBox.addItem(7);
		personCountComboBox.addItem(8);
		personCountComboBox.addItem(9);
		JLabel lblAnzahlPersonen = new JLabel("Erwachsene");
		lblAnzahlPersonen.setBounds(18, 70, 194, 24);
		
		childrenCountComboBox = new JComboBox<Integer>();
		childrenCountComboBox.setBounds(217, 98, 143, 23);
		childrenCountComboBox.addItem(0);
		childrenCountComboBox.addItem(1);
		childrenCountComboBox.addItem(2);
		childrenCountComboBox.addItem(3);
		childrenCountComboBox.addItem(4);
		
		JLabel lblChildrenCount = new JLabel("Kinder");
		lblChildrenCount.setBounds(18, 97, 194, 24);
		
		infantCountComboBox = new JComboBox<Integer>();
		infantCountComboBox.setBounds(217, 126, 143, 23);
		infantCountComboBox.addItem(0);
		infantCountComboBox.addItem(1);
		infantCountComboBox.addItem(2);
		infantCountComboBox.addItem(3);
		infantCountComboBox.addItem(4);
		JLabel lblInfantCount = new JLabel("Kleinkinder");
		lblInfantCount.setBounds(18, 126, 194, 24);
		
		tryCountComboBox = new JComboBox<Integer>();
		tryCountComboBox.setBounds(217, 154, 143, 23);
		tryCountComboBox.addItem(1);
		tryCountComboBox.addItem(3);
		tryCountComboBox.addItem(5);
		tryCountComboBox.addItem(10);
		tryCountComboBox.addItem(15);
		tryCountComboBox.addItem(30);
		tryCountComboBox.addItem(50);
		
		JLabel lblAnzahlVersuche = new JLabel("Versuche");
		lblAnzahlVersuche.setBounds(18, 154, 194, 24);
		
		frmProjektBlindflug.getContentPane().add(lblZiel);
		frmProjektBlindflug.getContentPane().add(cityComboBox);
		
		frmProjektBlindflug.getContentPane().add(lblAnzahlPersonen);
		frmProjektBlindflug.getContentPane().add(personCountComboBox);
		
		frmProjektBlindflug.getContentPane().add(lblChildrenCount);
		frmProjektBlindflug.getContentPane().add(childrenCountComboBox);
		
		frmProjektBlindflug.getContentPane().add(lblInfantCount);
		frmProjektBlindflug.getContentPane().add(infantCountComboBox);
		
		frmProjektBlindflug.getContentPane().add(lblAnzahlVersuche);
		frmProjektBlindflug.getContentPane().add(tryCountComboBox);
		
		frmProjektBlindflug.getContentPane().add(startSearchButton);
		frmProjektBlindflug.getContentPane().add(renewConfigButton);
		
		
		JLabel lblAbflug = new JLabel("Abflug");
		lblAbflug.setBounds(18, 16, 194, 24);
		frmProjektBlindflug.getContentPane().add(lblAbflug);

		departureComboBox = new JComboBox<Departure>();
		departureComboBox.setBounds(217, 17, 143, 23);
		frmProjektBlindflug.getContentPane().add(departureComboBox);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 351, 370, 198);
		frmProjektBlindflug.getContentPane().add(scrollPane);

		JEditorPane editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);

		this.logger = new WingsLogger(editorPane);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		desktopPane.setBackground(UIManager.getColor("Button.background"));
		desktopPane.setBounds(18, 183, 166, 117);
		frmProjektBlindflug.getContentPane().add(desktopPane);

		this.outwardDateChooser = new JDateChooser();
		outwardDateChooser.setBounds(43, 27, 114, 23);
		desktopPane.add(outwardDateChooser);
		this.outwardDateChooser.setDate(tomorrow);
		this.outwardDateChooser.setMaxSelectableDate(maxDate);

		this.outwardDateChooser.setMinSelectableDate(tomorrow);

		JLabel lblVon = new JLabel("zw.");
		lblVon.setBounds(10, 54, 42, 24);
		desktopPane.add(lblVon);

		JLabel lblToOutward = new JLabel("und");
		lblToOutward.setBounds(10, 81, 42, 24);
		desktopPane.add(lblToOutward);

		JLabel lblCloclFrom = new JLabel("Uhr");
		lblCloclFrom.setBounds(133, 54, 35, 24);
		desktopPane.add(lblCloclFrom);

		JLabel label = new JLabel("Uhr");
		label.setBounds(133, 81, 35, 24);
		desktopPane.add(label);

		JLabel outwardFlight = new JLabel("Hinflug");
		outwardFlight.setBounds(62, 0, 95, 24);
		desktopPane.add(outwardFlight);

		JDesktopPane returnPane = new JDesktopPane();
		returnPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		returnPane.setBackground(UIManager.getColor("Button.background"));
		returnPane.setBounds(194, 183, 166, 117);
		frmProjektBlindflug.getContentPane().add(returnPane);

		Date dateForFormSpinnerModel = new Date(1382824800346L);
		Date dateForToSpinnerModel = new Date(1382824741346L);

		this.outWardFromModel = new SpinnerDateModel(dateForFormSpinnerModel, null, null, Calendar.DAY_OF_MONTH);
		this.outWardFromTimeSpinner = new JSpinner(outWardFromModel);
		outWardFromTimeSpinner.setBounds(43, 54, 78, 23);
		desktopPane.add(outWardFromTimeSpinner);
		JSpinner.DateEditor outWardFromEditor = new JSpinner.DateEditor(outWardFromTimeSpinner, "HH:mm");
		outWardFromTimeSpinner.setEditor(outWardFromEditor);

		this.outWardToModel = new SpinnerDateModel(dateForToSpinnerModel, null, null, Calendar.DAY_OF_MONTH);
		this.outWardToTimeSpinner = new JSpinner(outWardToModel);
		outWardToTimeSpinner.setBounds(43, 81, 78, 23);
		desktopPane.add(outWardToTimeSpinner);
		JSpinner.DateEditor outwardToEditor = new JSpinner.DateEditor(outWardToTimeSpinner, "HH:mm");
		outWardToTimeSpinner.setEditor(outwardToEditor);

		this.returnFromModel = new SpinnerDateModel(dateForFormSpinnerModel, null, null, Calendar.DAY_OF_MONTH);
		this.returnFromTimeSpinner = new JSpinner(returnFromModel);
		returnFromTimeSpinner.setBounds(43, 54, 80, 23);
		returnPane.add(returnFromTimeSpinner);
		JSpinner.DateEditor returnFromEditor = new JSpinner.DateEditor(returnFromTimeSpinner, "HH:mm");
		returnFromTimeSpinner.setEditor(returnFromEditor);

		this.returnToModel = new SpinnerDateModel(dateForToSpinnerModel, null, null, Calendar.DAY_OF_MONTH);
		this.returnToTimeSpinner = new JSpinner(returnToModel);
		returnToTimeSpinner.setBounds(43, 81, 80, 23);
		returnPane.add(returnToTimeSpinner);
		JSpinner.DateEditor returnToEditor = new JSpinner.DateEditor(returnToTimeSpinner, "HH:mm");
		returnToTimeSpinner.setEditor(returnToEditor);

		this.returnDateChooser = new JDateChooser();
		this.returnDateChooser.setBounds(43, 27, 113, 23);
		returnPane.add(returnDateChooser);
		this.returnDateChooser.setDate(dayAfterTomorrow);
		this.returnDateChooser.setMaxSelectableDate(maxDate);
		this.returnDateChooser.setMinSelectableDate(dayAfterTomorrow);

		JLabel lblreturnFromClock = new JLabel("Uhr");
		lblreturnFromClock.setBounds(133, 54, 33, 24);
		returnPane.add(lblreturnFromClock);

		JLabel lblReturnToClock = new JLabel("Uhr");
		lblReturnToClock.setBounds(133, 81, 33, 29);
		returnPane.add(lblReturnToClock);

		JLabel lblReturnFrom = new JLabel("zw.");
		lblReturnFrom.setBounds(10, 54, 33, 24);
		returnPane.add(lblReturnFrom);

		JLabel lblReturnTo = new JLabel("und");
		lblReturnTo.setBounds(10, 81, 33, 24);
		returnPane.add(lblReturnTo);

		JLabel lblReturnFlight = new JLabel("R\u00FCckflug");
		lblReturnFlight.setBounds(62, 0, 95, 24);
		returnPane.add(lblReturnFlight);

		this.outwardDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				if ("date".equals(e.getPropertyName()))
				{
					Date newValue = (Date) e.getNewValue();
					Date returnDate = returnDateChooser.getDate();
					if (returnDate == null || newValue.after(returnDate))
					{
						newValue.setDate(newValue.getDate() + 1);
						returnDateChooser.setDate(newValue);
					}
				}
			}
		});

		this.departureComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event)
			{
				if (event.getStateChange() == ItemEvent.SELECTED)
				{
					Departure selectedDeparture = (Departure) event.getItem();
					populateCitiesComboBox(selectedDeparture);
				}
			}
		});

		this.startSearchButton.addActionListener(

		new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					logger.reset();
					commitSpinners();

					int personCount = (Integer) personCountComboBox.getSelectedItem();
					int childrenCount = (Integer) childrenCountComboBox.getSelectedItem();
					int infantCount = (Integer) infantCountComboBox.getSelectedItem();
					
					int tryCount = (Integer) tryCountComboBox.getSelectedItem();
					City city = (City) cityComboBox.getSelectedItem();

					Departure departure = (Departure) departureComboBox.getSelectedItem();

					Date outwardDate = outwardDateChooser.getDate();
					Date returnDate = returnDateChooser.getDate();

					Date outwardFromTime = outWardFromModel.getDate();
					Date outwardToTime = outWardToModel.getDate();

					Date returnFromTime = returnFromModel.getDate();
					Date returnToTime = returnToModel.getDate();

					DateTime outwardDateTime = new DateTime(outwardDate);
					DateTime returnDateTime = new DateTime(returnDate);

					DateTime outwardTimeFromDateTime = new DateTime(outwardFromTime);
					DateTime outwardTimeToDateTime = new DateTime(outwardToTime);
					DateTime returnTimeFromDateTime = new DateTime(returnFromTime);
					DateTime returnTimeToDateTime = new DateTime(returnToTime);

					DateInformation dateInformation = new DateInformation(outwardDateTime, returnDateTime);
					TimeRange outwardFlightTimeInfo = null;
					TimeRange returnFlightTimeInfo = null;

					Time outwardFrom = new Time(outwardTimeFromDateTime.getHourOfDay(), outwardTimeFromDateTime.getMinuteOfHour());
					Time outwardTo = new Time(outwardTimeToDateTime.getHourOfDay(), outwardTimeToDateTime.getMinuteOfHour());

					Time returnFrom = new Time(returnTimeFromDateTime.getHourOfDay(), returnTimeFromDateTime.getMinuteOfHour());
					Time returnTo = new Time(returnTimeToDateTime.getHourOfDay(), returnTimeToDateTime.getMinuteOfHour());

					outwardFlightTimeInfo = new TimeRange(outwardFrom, outwardTo);
					returnFlightTimeInfo = new TimeRange(returnFrom, returnTo);

					Searcher searcher = initSearch();
					searcher.search(
							tryCount,
							personCount,
							childrenCount,
							infantCount,
							departure,
							city,
							dateInformation,
							outwardFlightTimeInfo,
							returnFlightTimeInfo,
							logger);

				} catch (Exception e)
				{
					logger.attachLog(e.getMessage());
					e.printStackTrace();
				}
			}
		});

		this.renewConfigButton.addActionListener(

		new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				disableGui();
				ConfigurationProvider.reInitByGrabbingConfig(logger, getOperationFinishedCallBack());
			}
		});

		frmProjektBlindflug.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (navigator != null)
				{
					navigator.quit();
				}
				System.exit(0);
			}
		});

	}

	private void initDepartures()
	{
		this.departureComboBox.removeAllItems();
		for (Departure departure : ConfigurationProvider.getAllDepartures())
		{
			this.departureComboBox.addItem(departure);
		}
	}

	private void populateCitiesComboBox(Departure departure)
	{
		// some day someone will see this code and laugh really hard about it.
		// mit recht! I am one lazy motherfucker.

		if (departure == null) throw new NullPointerException("departure is null");
		this.cityComboBox.removeAllItems();

		HashSet<City> distinctedListOfCities = new HashSet<City>();

		ArrayList<City> sortedList = new ArrayList<City>();

		for (CategoryInfo categoryInfo : departure.getCategoryInfos())
		{
			for (City city : categoryInfo.getCities())
			{
				distinctedListOfCities.add(city);
			}
		}

		for (City city : distinctedListOfCities)
		{
			sortedList.add(city);
		}

		Collections.sort(sortedList);

		for (City city : sortedList)
		{
			distinctedListOfCities.add(city);
			this.cityComboBox.addItem(city);
		}
	}

	@Override
	public void onSearchEvent(SearcherEvent searcherEvent)
	{
		if(searcherEvent.getResult() != SearchResult.Sucess)
		{
			this.navigator.quit();
		}
		this.enableGui();
	}

	private void enableGui()
	{
		this.startSearchButton.setEnabled(true);
		this.renewConfigButton.setEnabled(true);
		this.frmProjektBlindflug.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void disableGui()
	{
		this.startSearchButton.setEnabled(false);
		this.renewConfigButton.setEnabled(false);
		this.frmProjektBlindflug.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	private Searcher initSearch()
	{
		if (this.searcher != null)
		{
			this.searcher.removeEventListener(this);
		}
		this.disableGui();
		GermanWingsNavigator navigator = this.getFreshNavigator();
		this.searcher = new Searcher(navigator);
		this.searcher.addEventListener(this);
		return searcher;
	}

	private GermanWingsNavigator getFreshNavigator()
	{
		if(this.navigator != null)
		{
			this.navigator.quit();
		}
		this.navigator = new GermanWingsNavigator();
		return this.navigator;
	}
	
	//do this by events for gods sake!
	@Override
	public void operationFinished(boolean sucess)
	{
		if (sucess)
		{
			initDepartures();
			this.logger.attachLog("Konfiguration erfolgreich aktualisiert");
		}
		enableGui();
	}

	//do this by events for gods sake!
	public IOperationFinishedCallBack getOperationFinishedCallBack()
	{
		return this;
	}
}
