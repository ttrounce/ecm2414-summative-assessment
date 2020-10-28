package ecm2414.cardgame.tests.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ecm2414.cardgame.tests.CardDeckTest;
import ecm2414.cardgame.tests.CardGameTest;
import ecm2414.cardgame.tests.CardGameUtilTest;
import ecm2414.cardgame.tests.CardInputTest;
import ecm2414.cardgame.tests.CardTest;
import ecm2414.cardgame.tests.PlayerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CardDeckTest.class, CardGameTest.class, CardGameUtilTest.class, CardInputTest.class, CardTest.class, PlayerTest.class })
public class CardGameSuite
{

}
