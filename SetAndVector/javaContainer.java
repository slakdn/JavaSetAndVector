
public interface javaContainer<T> {

    void add(T element);
    void remove(T element);
    int size();
    MyIterator<T> getIterator();

}