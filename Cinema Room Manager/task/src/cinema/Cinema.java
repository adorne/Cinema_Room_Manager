package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {

    private static int rows;

    private static int seatsInRow;

    private static final char freeSeat = 'S';

    private static final char occupiedSeat = 'B';

    private static final int seatsThreshold = 60;

    private static final int standardSeatPrice = 10;

    private static final int cheapSeatPrice = 8;

    public static int currentIncome;

    public static int seatsCount;

    public static int occupiedSeatsCount;

    private static char currency;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        setRows(Integer.parseInt(getAnswer(scanner, "Enter the number of rows:")));
        setSeatsInRow(Integer.parseInt(getAnswer(scanner, "Enter the number of seats in each row:")));
        setCurrency('$');
        char[][] cinema = createNewCinema(rows, seatsInRow);

        // total seats number
        setSeatsCount(countSeats(cinema));

        // menu logic
        boolean exitMenu = false;

        while (!exitMenu) {
            exitMenu = showMenu(scanner, cinema);
        }

        scanner.close();
    }

    public static void setRows(int rows) {
        Cinema.rows = rows;
    }

    public static void setSeatsInRow(int seatsInRow) {
        Cinema.seatsInRow = seatsInRow;
    }

    public static void setCurrency(char currency) {
        Cinema.currency = currency;
    }

    public static void setCurrentIncome(int currentIncome) {
        Cinema.currentIncome = currentIncome;
    }

    public static void setSeatsCount(int seatsCount) {
        Cinema.seatsCount = seatsCount;
    }

    public static void setOccupiedSeatsCount(int occupiedSeatsCount) {
        Cinema.occupiedSeatsCount = occupiedSeatsCount;
    }

    public static int getCurrentIncome() {
        return currentIncome;
    }

    public static int getSeatsCount() {
        return seatsCount;
    }

    public static int getOccupiedSeatsCount() {
        return occupiedSeatsCount;
    }

    private static String getAnswer(Scanner scanner, String prompt) {
        System.out.println(prompt);

        return scanner.nextLine().trim();
    }

    private static boolean showMenu(Scanner scanner, char[][] cinema) {
        System.out.print("\n1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit");

        int answer = Integer.parseInt(getAnswer(scanner, ""));

        switch (answer) {
            case 1:
                showCinema(cinema);
                break;

            case 2:
                buyTicket(scanner, cinema);
                setCurrentIncome(calculateCurrentIncome(cinema));
                break;

            case 3:
                showStatistics(cinema);
                break;

            case 0:
                return true;
        }

        return false;
    }

    private static void showStatistics(char[][] cinema) {
        System.out.println();
        System.out.printf("Number of purchased tickets: %d\n", getOccupiedSeatsCount());
        System.out.printf("Percentage: %.2f%c\n", (float) getOccupiedSeatsCount() / getSeatsCount() * 100, '%');
        System.out.printf("Current income: %c%d\n", '$', getCurrentIncome());
        System.out.printf("Total income: %c%d\n", '$', calculateTotalIncome(cinema));
    }

    private static boolean isSeatOccupied(char[][] cinema, int row, int seat) {
        return cinema[row - 1][seat - 1] == occupiedSeat;
    }

    private static boolean isSeatExists(char[][] cinema, int row, int seat) {
        return (seat > 0 && row > 0) && row <= cinema.length && seat <= cinema[row - 1].length;
    }

    private static int countOccupiedSeats(char[][] cinema) {
        int counter = 0;

        for (char[] row : cinema) {
            for (char seat : row) {
                if (seat == occupiedSeat) {
                    counter++;
                }
            }
        }

        return counter;
    }

    private static char[][] createNewCinema(int rows, int seats) {
        char[][] cinema = new char[rows][seats];

        for (char[] line : cinema) {
            Arrays.fill(line, Cinema.freeSeat);
        }

        return cinema;
    }

    private static void showCinema(char[][] cinema) {
        System.out.print("\nCinema:\n");

        for (int i = 0; i < cinema.length; i++) {
            for (int j = 0; j <= cinema[i].length; j++) {
                // print seat numbers
                if (i == 0 && j == 0) {
                    for (int k = 0; k <= cinema[i].length; k++) {
                        if (k == 0) {
                            System.out.printf("%s", "  ");
                        } else {
                            System.out.printf("%d%c", k, ' ');
                        }
                    }

                    System.out.println();
                }

                // print row numbers
                if (j == 0) {
                    System.out.printf("%d%c", i + 1, ' ');
                } else {
                    // shifted by +1
                    System.out.printf("%c%c", cinema[i][j - 1], ' ');
                }
            }

            System.out.println();
        }
    }

    public static int countSeats(char[][] cinema) {
        int sum = 0;

        for (char[] row : cinema) {
            for (char ignored : row) {
                sum++;
            }
        }

        return sum;
    }

    private static int calculateTotalIncome(char[][] cinema) {
        int profit = 0;

        for (int i = 0; i < cinema.length; i++) {
//            System.out.printf("%d row seat costs %d\n", i + 1, getRowSeatPrice(cinema, i));
            profit = profit + getRowSeatPrice(cinema, i) * cinema[i].length;
//            System.out.printf("total profit is %d\n", profit);
        }

        return profit;
    }

    private static int calculateCurrentIncome(char[][] cinema) {
        int currentIncome = 0;

        for (int i = 0; i < cinema.length; i++) {
            for (int j = 0; j < cinema[i].length; j++) {
                if (cinema[i][j] == occupiedSeat) {
                    currentIncome += getRowSeatPrice(cinema, i);
                }
            }
        }

        return currentIncome;
    }

    private static int getRowSeatPrice(char[][] cinema, int row) {
        int seatsNum = countSeats(cinema);
        int frontHalf = Cinema.rows / 2;

        if (seatsNum <= Cinema.seatsThreshold) {
            return Cinema.standardSeatPrice;
        } else {
            if (row < frontHalf) {
                return Cinema.standardSeatPrice;
            } else {
                return Cinema.cheapSeatPrice;
            }
        }
    }

    private static void selectSeat(char[][] cinema, int row, int seat) {
        cinema[row - 1][seat - 1] = Cinema.occupiedSeat;
    }

    private static void buyTicket(Scanner scanner, char[][] cinema) {
        boolean exitBuyTicket = false;

        while (!exitBuyTicket) {
            System.out.println();

            int selectedRow = Integer.parseInt(getAnswer(scanner, "Enter a row number:"));
            int selectedSeat = Integer.parseInt(getAnswer(scanner, "Enter a seat number in that row:"));

            if (!isSeatExists(cinema, selectedRow, selectedSeat)) {
                System.out.println("\nWrong input!");
            } else if (isSeatOccupied(cinema, selectedRow, selectedSeat)) {
                System.out.println("\nThat ticket has already been purchased");
            } else {
                int seatPrice = getRowSeatPrice(cinema, selectedRow - 1);

                System.out.printf("%cTicket price: %c%d%c", '\n', Cinema.currency, seatPrice, '\n');

                selectSeat(cinema, selectedRow, selectedSeat);
                setOccupiedSeatsCount(countOccupiedSeats(cinema));

                exitBuyTicket = true;
            }
        }
    }

}
