
import junit.framework.TestCase.*
import org.junit.Test

class ShipsTest {

    //Asio does it make sense to do the mock for system in and test everything
    @Test
    fun myTest() {
        val expected = 10
        val actual = 5 + 5

        assertEquals(expected, actual)
    }

    @Test
    fun allShipsAreDownOnFinishTest() {
        val gameData = GameData()
        gameData.playingShips.addAll(listOf(
            Ship("BATTLESHIP", 50, 5)))

        assertFalse(isGameFinished(gameData))

        for (i in gameData.playingShips) {
            while(i.isAlive())
                i.shot()
        }

        assertTrue(isGameFinished(gameData))
    }

    @Test
    fun placeOneShipOnBoardTest() {
        val board: Array<Array<Int>> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } }
        val ship = Ship("BATTLESHIP", 50, 5)
        val checksum = ship.signature * ship.size
        var sum = 0
        for(i in board) {
            for (j in i)
                sum += j
        }
        assertEquals(sum, checksum)
    }

    @Test
    fun allTheShipsArePlacedTest() {
        val gameData = GameData()
        val listOfShips = listOf(
            Ship("BATTLESHIP", 50, 5),
            Ship("DESTROYER1", 41, 4),
            Ship("DESTROYER2", 42, 4)
        )
        val checksum = listOfShips.sumOf { it.signature * it.size }
        prepareBoard(gameData, listOfShips)
        var sum = 0
        for(i in gameData.computerBoard) {
            for (j in i)
                sum += j
        }
        assertEquals(sum, checksum)
    }
}