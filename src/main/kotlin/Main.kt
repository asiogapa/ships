import java.util.*

const val BOARD_SIZE = 10

enum class Ship(val signature: Int, val size: Int) {
    BATTLESHIP(50, 5),
    DESTROYER1(41, 4),
    DESTROYER2(42, 4)
}

fun printBoard(board: Array<Array<Int>>) {
    println("Current board:")
    println("        A   B   C   D   E   F   G   H   I   J")
    println("     _________________________________________")
    for ((index, row) in board.withIndex()) {
        print("%3d | ".format(index+1))
        for (element in row) {
            print("%3d ".format(element))
        }
        println()
    }
    println()
}


// What to do if not possible to put another ship


fun checkShipPlacement(
    board: Array<Array<Int>>,
    ship: Ship,
    xCoordinate: Int,
    yCoordinate: Int,
    verticalPosition: Boolean
): Boolean {
    if (verticalPosition) {
        // ship won't fit
        if (xCoordinate > board.size - ship.size)
            return false
        // collision with other ship
        for (i in xCoordinate until xCoordinate + ship.size) {
            if (board[i][yCoordinate] != 0)
                return false
        }
        return true
    }
    else {
        // ship won't fit
        if (yCoordinate > board.size - ship.size)
            return false
        // collision with other ship
        for (i in yCoordinate until yCoordinate + ship.size) {
            if (board[xCoordinate][i] != 0)
                return false
        }
        return true
    }
}


fun randomlyPlaceShip(board: Array<Array<Int>>, ship: Ship) {
    var shipToPut = true
    while (shipToPut) {

        // choose vertical/horizontal placement
        val random = Random()
        val vertical = random.nextBoolean()

        // choose coordinates
        val xCoordinate = (0..9).random()
        val yCoordinate = (0..9).random()

        // Asio here I want to combine check and put better
        if(checkShipPlacement(board, ship, xCoordinate, yCoordinate, vertical)) {
            if (vertical) {
                for (i in xCoordinate until xCoordinate + ship.size)
                    board[i][yCoordinate] = ship.signature
            }
            else {
                for (i in yCoordinate until yCoordinate + ship.size)
                    board[xCoordinate][i] = ship.signature
            }
            shipToPut = false
        }
    }
}

fun play(computerBoard: Array<Array<Int>>, playerBoard: Array<Array<Int>>) {
    val scanner = Scanner(System.`in`)

    var battleShipDefence = Ship.BATTLESHIP.size
    var destroyer1Defence = Ship.DESTROYER1.size
    var destroyer2Defence = Ship.DESTROYER2.size

    while(true) {
        println("Make your shot:")
        val stringInput = scanner.nextLine()
        if(stringInput.length != 2) {
            println("Input format not recognized, please type one letter A-J followed by one number 1-10 e.g A3 and press enter")
            continue
        }
        val yCoordinate: Int = stringInput[0].uppercaseChar() - 'A'
        val xCoordinate: Int = stringInput[1].digitToInt() - 1
        if (xCoordinate >= BOARD_SIZE || xCoordinate < 0 || yCoordinate >= BOARD_SIZE || yCoordinate < 0) {
            println("Coordinates need to be within given range")
            continue
        }

        if (playerBoard[xCoordinate][yCoordinate] > 0) {
            println("Coordinates already checked, please try another one")
        }

        else if (computerBoard[xCoordinate][yCoordinate] == 0) {
            playerBoard[xCoordinate][yCoordinate] = 1
            println("Missed! Make another shot")
        }
        else if (computerBoard[xCoordinate][yCoordinate] == Ship.BATTLESHIP.signature) {
            battleShipDefence--
            playerBoard[xCoordinate][yCoordinate] = 9
        }
        else if (computerBoard[xCoordinate][yCoordinate] == Ship.DESTROYER1.signature) {
            destroyer1Defence--
            playerBoard[xCoordinate][yCoordinate] = 9
        }
        else if (computerBoard[xCoordinate][yCoordinate] == Ship.DESTROYER2.signature) {
            destroyer2Defence--
            playerBoard[xCoordinate][yCoordinate] = 9
        }

        // print when specific ship sinks

        if (battleShipDefence == 0 && destroyer1Defence == 0 && destroyer2Defence == 0) {
            println("All the ships are down, you won!")
            return
        }
        printBoard(playerBoard)
    }
}

fun prepareGame(board: Array<Array<Int>>) {
    randomlyPlaceShip(board, Ship.BATTLESHIP)
    randomlyPlaceShip(board, Ship.DESTROYER1)
    randomlyPlaceShip(board, Ship.DESTROYER2)
}

fun main(args: Array<String>) {

    // Asio TODOs

    // Make exit/quit button
    // Surround board with A-J, 1-10 description
    // consider changing display to * x
    // display current state of sink ships
    // add mechanism to inform that ship sink
    // Write tests (gradle build script?)
    // Add web UI?
    // Think about file structure
    // make sth like exec
    // readme
    // github upload

    val computerBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } }
    val playerBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { 0 } }

    println("Ships!")
    prepareGame(computerBoard)

    // Asio Does ship numbers must be unique?

    printBoard(computerBoard)
    //play(computerBoard, playerBoard)


}