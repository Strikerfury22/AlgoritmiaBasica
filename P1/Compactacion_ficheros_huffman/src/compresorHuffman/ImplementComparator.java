package compresorHuffman;

import java.util.Comparator;

public class ImplementComparator implements Comparator<Nodo> {
  public int compare(Nodo x, Nodo y) {
    return x.valor - y.valor;
  }
}
