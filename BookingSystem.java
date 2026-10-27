
class BookingSystem {
    private final boolean[] seats = new boolean[6];

    // Synchronized method prevents double booking
    public synchronized void bookSeat(String customer, int seatNo) {
        System.out.println(customer + " is trying to book Seat " + seatNo);

        if (seatNo < 1 || seatNo > seats.length) {
            System.out.println("Invalid seat number!");
            return;
        }

        if (!seats[seatNo - 1]) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            seats[seatNo - 1] = true;
            System.out.println("Booking successful for " + customer
                    + " | Seat: " + seatNo);
        } else {
            System.out.println("Booking failed for " + customer
                    + " | Seat " + seatNo + " is already booked.");
        }
    }

    public synchronized void displaySeats() {
        System.out.println("\nFinal Seat Status:");
        for (int i = 0; i < seats.length; i++) {
            System.out.println("Seat " + (i + 1) + ": "
                    + (seats[i] ? "Booked" : "Available"));
        }
    }
}

class Customer extends Thread {
    private final BookingSystem system;
    private final int seatNo;

    public Customer(String name, BookingSystem system, int seatNo) {
        super(name);
        this.system = system;
        this.seatNo = seatNo;
    }

    @Override
    public void run() {
        system.bookSeat(getName(), seatNo);
    }
}

public class Wayfare {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();

        System.out.println("===== WAYFARE TICKET BOOKING SYSTEM =====\n");

        Customer c1 = new Customer("Customer 1", system, 2);
        Customer c2 = new Customer("Customer 2", system, 2);
        Customer c3 = new Customer("Customer 3", system, 4);
        Customer c4 = new Customer("Customer 4", system, 2);

        c1.start();
        c2.start();
        c3.start();
        c4.start();

        try {
            c1.join();
            c2.join();
            c3.join();
            c4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Booking process interrupted.");
        }

        system.displaySeats();
    }
}
