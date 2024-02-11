
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
/**
 * A generic set implementation that supports adding, removing, and iterating over elements.
 * @param <T> The type of elements in the set, must extend Comparable for sorting.
 */
public class JavaSet<T extends Comparable<T> > implements javaContainer<T>  {
    private Object[] elements;
    private int size;

    /**
     * Constructs an empty set with an initial capacity of 10.
     */
    public JavaSet() {                                        
        this.elements = new Object[10];
        this.size = 0;
    }
    
    /**
     * Adds the specified element to the set if it is not already present.
     * Elements are added in sorted order.
     *
     * @param element The element to be added to the set.
     * @throws IllegalStateException If the element is already present in the set.
     */
    @SuppressWarnings("unchecked")
    public void add(T element) {
        if (!contains(element)) {               
            ensureCapacity();

            int insertIndex = 0;
            while(insertIndex < size && compare(element, (T) elements[insertIndex]) > 0 ){
                insertIndex++;
            }

            for(int i = size-1 ; i>=insertIndex ; i--) {
                elements[i+1] = elements[i];
            }

            elements[insertIndex] = element;
            size++;
        } else {                                       
            throw new IllegalStateException("It contains the element in the set already");
        }
    }

    private int compare(T element1, T element2) {
        return element1.compareTo(element2);
    }

    /**
     * Removes the specified element from the set.
     *
     * @param element The element to be removed from the set.
     * @throws NoSuchElementException If the element is not found in the set.
     */
    public void remove(T element) {
        int foundIndex = -1;

        // Finding the index of the element to be removed
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                foundIndex = i;
                break;
            }
        }

        // If the element was found, removing it by shifting elements
        if (foundIndex != -1) {
            for (int i = foundIndex; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }

            // Setting the last element to null
            elements[size - 1] = null;

            // Decreasing the size of the array
            size--;
        }else{
            throw new NoSuchElementException("Element not found in the set.");
        }
    }

    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in the set.
     *
     * @return An iterator.
     */
    @Override
    public MyIterator<T> getIterator() {               
        return new SetIterator();
    }

    /**
     * Checks if the set contains the specified element.
     *
     * @param element The element to be checked for presence in the set.
     * @return {@code true} if the set contains the element, {@code false} otherwise.
     */
    private boolean contains(T element) {
        for (var i = 0; i < size; i++) {
            if (areEqual(elements[i], element)) {
                return true;
            }
        }
        return false;
    }

    private boolean areEqual(Object element, Object element2) {
        if (element == null) {
            return element2 == null;
        } else {
            return element.equals(element2);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        var otherSet = (JavaSet<?>) obj;

        if (size != otherSet.size) return false;

        for (var i = 0; i < size; i++) {
            if (!areEqual(elements[i], otherSet.elements[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures that the set has sufficient capacity. If the capacity is not enough,
     * the set is resized to twice its current capacity.
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            var newCapacity = elements.length * 2;
            var newElements = new Object[newCapacity];

            MyIterator<T> iterator = getIterator();
            for (int i = 0; i < size; i++) {
                newElements[i] = iterator.next();
            }

            elements = (T[]) newElements;
        }
    }

    /**
     * Iterator implementation for the set.
     */
    private class SetIterator implements MyIterator<T> {
        private int index = 0;

        /**
         * Checks if there is another element in the set.
         *
         * @return {@code true} if there is another element, {@code false} otherwise.
         */
        public boolean hasNext() {               
            return index < size;
        }

        /**
         * Returns the next element in the set.
         *
         * @return The next element.
         * @throws NoSuchElementException If there are no more elements in the set.
         */
        @SuppressWarnings("unchecked")
        public T next() {
            if (hasNext()) {
                return (T) elements[index++];
            } else {
                throw new NoSuchElementException("No more elements in the set.");
            }
        }
    }

    public String toString() {
        var result = new StringBuilder("[");
        
        MyIterator<T> iterator = getIterator();
        while (iterator.hasNext()) {
            result.append(iterator.next());

            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    public int hashCode(){
        int result = Objects.hash(size);

        MyIterator<T> iterator = getIterator();
        while (iterator.hasNext()) {
            result = 31 * result + (iterator.next() != null ? iterator.next().hashCode() : 0);
        }

        return result;
    }

    private static void saveSetToFile(JavaSet<String> set, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(set.toString());
            System.out.println("Set saved to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error saving set to file: " + e.getMessage());
        }
    }

    /**
     * The main method to interactively test the JavaSet.
     *
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {
        var set = new JavaSet<String>();
        var scanner = new Scanner(System.in);

        try{
            System.out.print("Enter the size of the set: ");
            int setSize = scanner.nextInt();

            while (true) {
                System.out.println();
                System.out.println("Set Elements: " + set);
                System.out.println("1. Add element");
                System.out.println("2. Remove element");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                var choice = scanner.nextInt();
                scanner.nextLine();                         
    
                switch (choice) {
                    case 1:
                        for (int i = 0; i < setSize; i++) {
                            System.out.print("Enter element " + (i + 1) + ": ");
                            String elementToAdd = scanner.nextLine();
                            set.add(elementToAdd);
                            System.out.println("Element added successfully!");
                        }
                        saveSetToFile(set, "set.txt");
                        break;
                    case 2:
                        System.out.print("Enter element to remove: ");
                        var elementToRemove = scanner.nextLine();
                        set.remove(elementToRemove);
                        System.out.println("Element removed successfully!");
                        break;
                    case 3:
                        System.out.println("Exiting program. Bye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }finally{
            scanner.close();
        }
    }
}