import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * A generic vector implementation that supports adding, removing, and iterating over elements.
 *
 * @param <T> The type of elements in the vector.
 */
public class JavaVector<T> implements javaContainer<T> {
    private Object[] elements;
    private int size;

    /**
     * Constructs an empty vector with an initial capacity of 10.
     */
    public JavaVector() {
        this.elements = new Object[10];
        this.size = 0;
    }

    /**
     * Adds the specified element to the vector.
     *
     * @param element The element to be added to the vector.
     */
    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    /**
     * Removes the specified element from the vector.
     *
     * @param element The element to be removed from the vector.
     * @throws NoSuchElementException If the element is not found in the vector.
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
            elements[size - 1] = null;
            size--;
        }else{
            throw new NoSuchElementException("Element not found in the set.");
        }
    }

    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in the vector.
     *
     * @return An iterator.
     */
    @Override
    public MyIterator<T> getIterator() {
        return new VectorIterator();
    }

    /**
     * Checks if two elements are equal.
     *
     * @param element  The first element to be compared.
     * @param element2 The second element to be compared.
     * @return {@code true} if the elements are equal, {@code false} otherwise.
     */
    private boolean areEqual(Object element, Object element2) {
        if (element == null) {
            return element2 == null;
        } else {
            return element.equals(element2);
        }
    }

    /**
     * Compares two vectors for equality.
     *
     * @param obj The object to compare with.
     * @return {@code true} if the vectors are equal, {@code false} otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        var otherVector = (JavaVector<?>) obj;

        if (size != otherVector.size) return false;

        for (var i = 0; i < size; i++) {
            if (!areEqual(elements[i], otherVector.elements[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures that the vector has sufficient capacity. If the capacity is not enough,
     * the vector is resized to twice its current capacity.
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
     * Iterator implementation for the vector.
     */
    private class VectorIterator implements MyIterator<T> {
        private int index = 0;

        /**
         * Checks if there is another element in the vector.
         *
         * @return {@code true} if there is another element, {@code false} otherwise.
         */
        public boolean hasNext() {
            return index < size;
        }

        /**
         * Returns the next element in the vector.
         *
         * @return The next element.
         * @throws NoSuchElementException If there are no more elements in the vector.
         */
        @SuppressWarnings("unchecked")
        public T next() {
            if (hasNext()) {
                return (T) elements[index++];
            } else {
                throw new NoSuchElementException("No more elements in the vector.");
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

    private static void saveVecToFile(JavaVector<String> vector, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(vector.toString());
            System.out.println("Set saved to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error saving set to file: " + e.getMessage());
        }
    }

    /**
     * The main method to interact with the vector using user input.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        var vector = new JavaVector<String>();
        var scanner = new Scanner(System.in);

        try{
            System.out.print("Enter the size of the vector: ");
            int vecSize = scanner.nextInt();
            while (true) {
                System.out.println();
                System.out.println("Vector Elements: " + vector);
                System.out.println("1. Add element");
                System.out.println("2. Remove element");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                var choice = scanner.nextInt();
                scanner.nextLine();  
    
                switch (choice) {
                    case 1:
                        for (int i = 0; i < vecSize; i++) {
                            System.out.print("Enter element " + (i + 1) + ": ");
                            String elementToAdd = scanner.nextLine();
                            vector.add(elementToAdd);
                            System.out.println("Element added successfully!");
                        }
                        saveVecToFile(vector, "vector.txt");
                        break;
                    case 2:
                        System.out.print("Enter element to remove: ");
                        var elementToRemove = scanner.nextLine();
                        vector.remove(elementToRemove);
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