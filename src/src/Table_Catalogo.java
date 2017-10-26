/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author humbe
 */
public class Table_Catalogo extends AbstractTableModel {
     public static ArrayList<ItemCatalogo> lista;
    //private final ClassDAO<Veiculo> veiculo;
    private final String[] colunas;
 
    public Table_Catalogo(ArrayList<ItemCatalogo> cat) {
        colunas = new String[]{"Titulo", "Autor", "Jornal", "Volume", "Numero", "Paginas", "Ano", "Publicador"};
        //veiculo = new ClassDAO(Veiculo.class);
        lista = cat;
    }
 
    public void inserirVeiculo(ItemCatalogo veiculo) {
        lista.add(veiculo);
        fireTableDataChanged();
    }
 
    public void retirarVeiculo(int rownIndex) {
        lista.remove(rownIndex);
        fireTableDataChanged();
    }
 
    public ItemCatalogo selecionarVeiculo(int rownIndex) {
        return lista.get(rownIndex);
    }
 
    @Override
    public int getRowCount() {
        return lista.size();
    }
 
    @Override
    public int getColumnCount() {
        return colunas.length;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return lista.get(rowIndex).getTitle();
            case 1:
                return lista.get(rowIndex).getAuthor();
            case 2:
                return lista.get(rowIndex).getJournal();
            case 3:
                return lista.get(rowIndex).getVolume();
            case 4:
                return lista.get(rowIndex).getNumber();
            case 5:
                return lista.get(rowIndex).getPages();
            case 6:
                return lista.get(rowIndex).getYear();
            case 7:
                return lista.get(rowIndex).getPublisher();
            default:
                return lista.get(rowIndex);
        }
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }
}
