package p1;
import java.util.*;

// ===== Passenger Class =====
class Passenger {
    int ticket;
    String name;
    int age;
    Passenger next;

    Passenger(int t, String n, int a)
    {
        ticket = t;
        name = n;
        age = a;
        next = null;
    }

    // Merge Sort by Name
    static Passenger mergeSort(Passenger head) {
        if (head == null || head.next == null) return head;

        Passenger mid = getMiddle(head);
        Passenger next = mid.next;
        mid.next = null;

        Passenger left = mergeSort(head);
        Passenger right = mergeSort(next);

        return merge(left, right);
    }

    static Passenger merge(Passenger a, Passenger b) {
        if (a == null) return b;
        if (b == null) return a;

        Passenger result;
        if (a.name.compareToIgnoreCase(b.name) <= 0) {
            result = a;
            result.next = merge(a.next, b);
        } else {
            result = b;
            result.next = merge(a, b.next);
        }
        return result;
    }

    static Passenger getMiddle(Passenger head) {
        Passenger slow = head;
        Passenger fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}

// ===== Flight Class =====
class Flight {
    int id;
    String source;
    String destination;
    Passenger head;
    static int ticketCounter = 1000;

    Flight(int id, String s, String d) {
        this.id = id;
        source = s;
        destination = d;
    }

    void addPassenger(String name, int age) {
        Passenger p = new Passenger(ticketCounter++, name, age);
        if (head == null) head = p;
        else {
            Passenger temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = p;
        }
        System.out.println("Ticket Generated: " + p.ticket);
    }

    void displayPassengers() {
        Passenger temp = head;
        while (temp != null) {
            System.out.println(temp.ticket + " " + temp.name + " " + temp.age);
            temp = temp.next;
        }
    }

    Passenger searchPassenger(int ticket) {
        Passenger temp = head;
        while (temp != null) {
            if (temp.ticket == ticket) return temp;
            temp = temp.next;
        }
        return null;
    }

    Passenger deletePassenger(int ticket) {
        Passenger temp = head;
        Passenger prev = null;

        while (temp != null && temp.ticket != ticket) {
            prev = temp;
            temp = temp.next;
        }

        if (temp == null) return null;

        if (prev == null) head = temp.next;
        else prev.next = temp.next;

        return temp;
    }

    void sortPassengers() {
        head = Passenger.mergeSort(head);
    }
}

// ===== PassengerStack Class =====
class PassengerStack {
    Stack<Passenger> stack = new Stack<>();

    void push(Passenger p) { stack.push(p); }
    Passenger pop() { return stack.isEmpty() ? null : stack.pop(); }
}

// ===== BoardingQueue Class =====
class BoardingQueue {
    Queue<Passenger> q = new LinkedList<>();

    void addPassenger(Passenger p) {
        q.add(p);
        System.out.println("Added to boarding queue");
    }

    void boardPassenger() {
        if (q.isEmpty()) {
            System.out.println("Queue empty");
            return;
        }
        Passenger p = q.remove();
        System.out.println("Passenger Boarded: " + p.name);
    }

    void display() {
        for (Passenger p : q)
            System.out.println(p.ticket + " " + p.name);
    }
}

// ===== BaggageHash Class =====
class BaggageHash {
    static class Baggage {
        int tag;
        int ticket;
        double weight;
        Baggage(int t, int ti, double w) {
            tag = t; ticket = ti; weight = w;
        }
    }

    static Baggage table[] = new Baggage[10];

    static int midSquareHash(int key) {
        int sq = key * key;
        int mid = (sq / 10) % 10;
        return mid % table.length;
    }

    static void addBaggage(int ticket, double weight) {
        int tag = 1000 + (int)(Math.random() * 9000);
        int index = midSquareHash(tag);
        while (table[index] != null)
            index = (index + 1) % table.length;
        table[index] = new Baggage(tag, ticket, weight);
        System.out.println("Baggage Added at index:" + index);
    }

    static void display() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null)
                System.out.println(i + " -> Tag:" + table[i].tag + " Ticket:" + table[i].ticket);
            else
                System.out.println(i + " -> Empty");
        }
    }
}

// ===== Main Class =====
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Flight flights[] = new Flight[20];
        int count = 0;

        PassengerStack undo = new PassengerStack();
        BoardingQueue queue = new BoardingQueue();

        int ch;
        do {
            System.out.println("\n===== AIRLINE SYSTEM =====");
            System.out.println("1 Add Flight");
            System.out.println("2 Display Flights");
            System.out.println("3 Book Passenger");
            System.out.println("4 Display Passengers");
            System.out.println("5 Search Passenger");
            System.out.println("6 Delete Passenger");
            System.out.println("7 Undo Delete");
            System.out.println("8 Sort Passengers");
            System.out.println("9 Add to Boarding Queue");
            System.out.println("10 Board Passenger");
            System.out.println("11 Exit");

            ch = sc.nextInt();

            switch(ch) {
                case 1:
                    System.out.print("Flight ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Source: ");
                    String s = sc.nextLine();
                    System.out.print("Destination: ");
                    String d = sc.nextLine();
                    flights[count++] = new Flight(id, s, d);
                    System.out.println("Flight added successfully");
                    break;

                case 2:
                    for (int i = 0; i < count; i++)
                        System.out.println(flights[i].id + " " + flights[i].source + " -> " + flights[i].destination);
                    break;

                case 3:
                    System.out.print("Flight ID: ");
                    int fid = sc.nextInt();
                    Flight f = searchFlight(flights, count, fid);
                    if (f != null) {
                        sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Age: ");
                        int age = sc.nextInt();
                        f.addPassenger(name, age);
                        System.out.println("Booking Successful");
                    } else System.out.println("Flight not found");
                    break;

                case 4:
                    System.out.print("Flight ID: ");
                    int df = sc.nextInt();
                    Flight f1 = searchFlight(flights, count, df);
                    if (f1 != null) f1.displayPassengers();
                    else System.out.println("Flight not found");
                    break;

                case 5:
                    System.out.print("Flight ID: ");
                    int sf = sc.nextInt();
                    Flight f2 = searchFlight(flights, count, sf);
                    if (f2 != null) {
                        System.out.print("Ticket: ");
                        int tk = sc.nextInt();
                        Passenger p = f2.searchPassenger(tk);
                        if (p != null) System.out.println("Found: " + p.name);
                        else System.out.println("Passenger not found");
                    } else System.out.println("Flight not found");
                    break;

                case 6:
                    System.out.print("Flight ID: ");
                    int del = sc.nextInt();
                    Flight f3 = searchFlight(flights, count, del);
                    if (f3 != null) {
                        System.out.print("Ticket: ");
                        int t = sc.nextInt();
                        Passenger dp = f3.deletePassenger(t);
                        if (dp != null) {
                            undo.push(dp);
                            System.out.println("Deleted Successfully");
                        } else System.out.println("Passenger not found");
                    } else System.out.println("Flight not found");
                    break;

                case 7:
                    Passenger up = undo.pop();
                    if (up != null) {
                        System.out.println("Undo Passenger: " + up.name);
                        // Optionally restore passenger to a flight if desired
                    } else System.out.println("Nothing to undo");
                    break;

                case 8:
                    System.out.print("Flight ID: ");
                    int so = sc.nextInt();
                    Flight f4 = searchFlight(flights, count, so);
                    if (f4 != null) {
                        f4.sortPassengers();
                        System.out.println("Passengers sorted");
                    } else System.out.println("Flight not found");
                    break;

                case 9:
                    System.out.print("Ticket: ");
                    int bt = sc.nextInt();
                    for (int i = 0; i < count; i++) {
                        Passenger temp = flights[i].head;
                        while (temp != null) {
                            if (temp.ticket == bt) {
                                queue.addPassenger(temp);
                                break;
                            }
                            temp = temp.next;
                        }
                    }
                    break;

                case 10:
                    queue.boardPassenger();
                    break;
            }

        } while (ch != 11);
    }

    public static Flight searchFlight(Flight flights[], int count, int id) {
        for (int i = 0; i < count; i++) {
            if (flights[i].id == id) return flights[i];
        }
        return null;
    }
}