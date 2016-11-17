/**
 * Baroness.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse;

import krconverse.baroness.controller.DealCardsController;
import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

/**
 * An implementation for the Baroness variant of Solitaire, also known as
 * Thirteens.
 * <p>
 * Layout: There is a deck, five columns and a foundation pile. All of the cards
 * start face down in the deck.
 * <p>
 * Objective: To move all cards into the foundation pile by pairing the top
 * cards on the columns which add to thirteen or which are Kings.
 * <p>
 * Play: Deal five cards from the deck onto the top of the five columns. Pairs
 * of cards adding up to thirteen or single Kings can be moved to the foundation
 * pile after all five cards have been dealt. When there are no more moves to be
 * made, five more cards can be dealt and the game play continues like this
 * until the deck has run out (there are only two cards in the last deal) and
 * there are no more moves to be made.
 * <p>
 * Score: A player's score is determined by how many cards are left in play.
 * This means that the goal of the game is to achieve a low or zero score.
 */
public class Baroness extends Solitaire {

	/** deck which cards are dealt from */
	Deck deck;
	/** columns which cards are dealt to and played from */
	Column[] columns = new Column[5];
	/** pile which pairs are moved to after being played */
	Pile foundation;

	/** view for {@code deck} */
	DeckView deckView;
	/** views for {@code columns} */
	ColumnView[] columnViews = new ColumnView[5];
	/** view for {@code foundation} */
	PileView foundationView;

	/** view to show the score */
	IntegerView scoreView;
	/** view to show number of cards left in {@code deck} */
	IntegerView cardsLeftView;

	/**
	 * Creates a new Baroness Solitaire game.
	 */
	public Baroness() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#initialize()
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
	}

	/**
	 * Initializes the model for the plugin.
	 * 
	 * @param seed
	 *            The seed to start the model with for random generation.
	 */
	private void initializeModel(int seed) {
		// add the deck to the model
		deck = new Deck("deck");
		deck.create(seed);
		model.addElement(deck);
		
		// add the columns to the model
		for (int i = 0; i < 5; i++) {
			columns[i] = new Column("col" + (i + 1));
			model.addElement(columns[i]);
		}

		// add the foundation to the model
		foundation = new Pile("foundation");
		model.addElement(foundation);
		
		// update the score and cards left
		this.updateScore(52);
		this.updateNumberCardsLeft(52);
	}

	/**
	 * Initializes the view for the plugin.
	 */
	private void initializeView() {
		CardImages ci = getCardImages(); // the images which the cards will be
											// represented with

		int cardSpacing = 20;
		int extraTableauSpacing = 20;

		// show the deck on the screen
		deckView = new DeckView(deck);
		deckView.setBounds(cardSpacing, cardSpacing, ci.getWidth(), ci.getHeight());
		addViewWidget(deckView);

		// show the columns on the screen
		int maximumColumnHeight = 13 * ci.getOverlap() + ci.getHeight(); // maximum
																			// height
																			// in
																			// Baroness
		for (int i = 0; i < 5; i++) {
			columnViews[i] = new ColumnView(columns[i]);
			columnViews[i].setBounds(extraTableauSpacing + cardSpacing + (i + 1) * (cardSpacing + ci.getWidth()), // x
					cardSpacing, // y
					ci.getWidth(), // width
					maximumColumnHeight // height
			);
			addViewWidget(columnViews[i]);
		}

		// show the foundation pile on the screen
		foundationView = new PileView(foundation);
		foundationView.setBounds(extraTableauSpacing * 2 + cardSpacing + 6 * (cardSpacing + ci.getWidth()), // x
				cardSpacing, // y
				ci.getWidth(), // width
				ci.getHeight()); // height
		addViewWidget(foundationView);

		// show the score on screen
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(extraTableauSpacing * 2 + cardSpacing + 6 * (cardSpacing + ci.getWidth()),
				cardSpacing * 2 + ci.getHeight(), ci.getWidth(), 60);
		addViewWidget(scoreView);

		cardsLeftView = new IntegerView(getNumLeft());
		cardsLeftView.setBounds(cardSpacing, cardSpacing * 2 + ci.getHeight(), ci.getWidth(), 60);
		addViewWidget(cardsLeftView);
	}

	/**
	 * Initializes the controllers for the plugin.
	 */
	private void initializeControllers() {
		deckView.setMouseAdapter(new DealCardsController(this, model));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#getName()
	 */
	@Override
	public String getName() {
		return "Baroness";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#hasWon()
	 */
	@Override
	public boolean hasWon() {
		return getScoreValue() == 0;
	}

	/** 
	 * Launches the Baroness Solitaire variation.
	 */
	public static void main (String []args) {
		Main.generateWindow(new Baroness(), (int) System.currentTimeMillis());
	}
}
