import java.util.*

const val BOARD_SIZE = 10
const val SHOT_SHIP = 9
const val MISSED_SHOT = 1


class Ship(val name: String, val signature: Int, val size: Int) {
    private var shotsToTake = size

    fun shot() {
        shotsToTake--
    }

    fun isDown(): Boolean = shotsToTake <= 0
    fun isAlive(): Boolean = !(isDown())
}

data class GameData(
    val computerBoard: Array<Array<Int>> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } },
    val playerBoard: Array<Array<Int>> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } },
    val playingShips: MutableList<Ship> = mutableListOf(),
    var round: Int = 0
)


fun isGameFinished(gameData: GameData): Boolean =
    gameData.playingShips.all { it.isDown() }

fun printDownShips(gameData: GameData) {
    for (ship in gameData.playingShips) {
        if (ship.isDown()) {
            print("${ship.name} is down.")
        }
    }
    println()
}
fun printBoard(board: Array<Array<Int>>) {
    println("Current board:")
    println("        A   B   C   D   E   F   G   H   I   J")
    println("     _________________________________________")
    for ((index, row) in board.withIndex()) {
        print("%3d | ".format(index+1))
        for (element in row) {
            if (element == MISSED_SHOT)
                print("%3c ".format('x'))
            else if (element == SHOT_SHIP)
                print("%3c ".format('*'))
            else
                print("%3d ".format(element))
        }
        println()
    }
    println()
}

fun checkShipPlacement(
    board: Array<Array<Int>>,
    ship: Ship,
    xCoordinate: Int,
    yCoordinate: Int,
    verticalPosition: Boolean
): List<Pair<Int,Int>>? {
    val cells = arrayListOf<Pair<Int,Int>>()
    if (verticalPosition) {
        // ship won't fit
        if (xCoordinate > board.size - ship.size)
            return null
        // collision with other ship
        for (i in xCoordinate until xCoordinate + ship.size) {
            cells.add(Pair(i, yCoordinate))
            if (board[i][yCoordinate] != 0)
                return null
        }
    }
    else {
        // ship won't fit
        if (yCoordinate > board.size - ship.size)
            return null
        // collision with other ship
        for (i in yCoordinate until yCoordinate + ship.size) {
            cells.add(Pair(xCoordinate,i))
            if (board[xCoordinate][i] != 0)
                return null
        }
    }
    return cells
}


fun randomlyPlaceShip(board: Array<Array<Int>>, ship: Ship) {
    var cells: List<Pair<Int,Int>>? = null
    while (cells == null) {

        // choose vertical/horizontal placement
        val random = Random()
        val vertical = random.nextBoolean()

        // choose coordinates
        val xCoordinate = (0..9).random()
        val yCoordinate = (0..9).random()

        // Asio here I want to combine check and put better
        cells = checkShipPlacement(board, ship, xCoordinate, yCoordinate, vertical)
    }
    cells.forEach { (x,y) -> board[x][y] = ship.signature }
}

fun shootShip(gameData: GameData, xCoordinate: Int, yCoordinate: Int) {
    for (ship in gameData.playingShips) {
        if (gameData.computerBoard[xCoordinate][yCoordinate] == ship.signature) {
            ship.shot()
            gameData.playerBoard[xCoordinate][yCoordinate] = SHOT_SHIP
            return
        }
    }
    // Asio return unknown error?
    return
}

fun play(scanner: Scanner, gameData: GameData) {
    while(true) {
        if (isGameFinished(gameData)) {
            winView()
            return
        }
        gameData.round++
        currGameStateView(gameData)
        println("Make your shot:")
        val stringInput = scanner.nextLine()

        if(stringInput.length < 2 || stringInput.length > 3) {
            println("Input format not recognized, please type one letter A-J followed by one number 1-10 e.g A3 and press enter")
            continue
        }

        val yCoordinate: Int = stringInput[0].uppercaseChar() - 'A'
        val xCoordinate: Int = stringInput.drop(1).toInt() - 1

        if (xCoordinate >= BOARD_SIZE || xCoordinate < 0 || yCoordinate >= BOARD_SIZE || yCoordinate < 0) {
            println("Coordinates need to be within given range ([A-J][1-10])")
        }
        else if (gameData.playerBoard[xCoordinate][yCoordinate] > 0) {
            println("Coordinates already checked, try different one")
        }
        else if (gameData.computerBoard[xCoordinate][yCoordinate] == 0) {
            gameData.playerBoard[xCoordinate][yCoordinate] = 1
            println("Missed! Make another shot")
        }
        else {
            shootShip(gameData, xCoordinate, yCoordinate)
            println("Nice shot!")
        }
    }
}

fun prepareBoard(gameData: GameData, ships: List<Ship>) {
    gameData.playingShips.addAll(ships)
    for(ship in gameData.playingShips)
        randomlyPlaceShip(gameData.computerBoard, ship)
}

fun startView() {
    println("Welcome to the ships game!")
    println("To begin game press b")
    println("To exit press any key")
}

fun currGameStateView(gameData: GameData) {
    println()
    println("~~~~~~~~~~~~~~~~~~    ROUND ${gameData.round}    ~~~~~~~~~~~~~~~~~~")
    printDownShips(gameData)
    printBoard(gameData.playerBoard)
}

fun winView() {
    println()
    println("**********************************")
    println("All the ships are down, you win!")
    println("**********************************")
}
fun endViewAndPromptForRetry(scanner: Scanner): Boolean {
    println()
    println("Wanna retry? [y/n]")
    if (scanner.nextLine() == "y")
        return true
    else
        println("Thanks for playing,")
        println("hope to see you soon!")
    return false
}
fun main() {

    val listOfShips = listOf(
        Ship("BATTLESHIP", 50, 5),
        Ship("DESTROYER1", 41, 4),
        Ship("DESTROYER2", 42, 4)
    )
    val scanner = Scanner(System.`in`)
    var retry = true

    while(retry) {
        startView()
        val beginInput =  scanner.nextLine()

        if (beginInput == "b") {
            val gameData = GameData()
            prepareBoard(gameData, listOfShips)
            // Asio just for debugging purposes
            // b
            // printBoard(gameData.computerBoard)
            play(scanner, gameData)
        }
        retry = endViewAndPromptForRetry(scanner)
    }
}



// Asio TODOs

// Write tests (gradle build script?)
// Think about file structure
// readme
// gitHub upload

// DONE
// Make exit/quit button
// Surround board with A-J, 1-10 description
// consider changing display to * x
// display current state of sink ships
// add mechanism to inform that ship sink
// Add web UI?