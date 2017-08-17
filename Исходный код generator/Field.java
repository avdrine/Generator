package generator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс контейнера данных, включающий в себя совокупность столбцов класса Column 
 * @author avdrine
 */
public class Field {
    
    /** Столбцы */
    private Column[] mColumns;
    /** Максимальная высота столбцов */
    private int mHeight;
    /** Ширина поля столбцов */
    private int mWidth;
    
    
    /** 
     * Конструктор
     * @param columns Столбцы, из которых будет формироваться строчка
     */
    Field(LinkedHashMap<String, Integer> columns)
    {
        mColumns = new Column[columns.size()];
        
        int i = 0;
        for (Iterator<Map.Entry<String, Integer>> it = columns.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry;
            entry = it.next();
            int width = (int) entry.getValue();
            String text = (String) entry.getKey();
            
            mColumns[i] = new Column(text, width); 
            i++;
        } 
       
        calculateHeight();
        calculateWidth();
    }
    
    /** 
     * Получение значения ширины
     * @return Ширина
     */
    public int getWidth()
    {
        return mWidth;
    }
    
    /** 
     * Получение значения высоты
     * @return Высота
     */
    public int getHeight()
    {
        return mHeight;
    }
    
    
    /** 
     * Расчет длины поля
     */
    private void calculateWidth()
    {
        for(int i = 0; i < mColumns.length; i++)
        {
            mWidth += mColumns[i].getWidth();
        }
        mWidth += 4 + (mColumns.length-1) * 3;
    }
    
    
    /** 
     * Расчет высоты поля
     */
    private void calculateHeight()
    {
        int maxHeight = 0;
        for (Column mColumn : mColumns) {
            if (mColumn.getHeight() > maxHeight) {
                maxHeight = mColumn.getHeight();
            }
        }
        mHeight = maxHeight;
    }
    
    @Override
    /** 
     * Формирование текстового вида поля
     * @return Поле в текстовом виде
     */
    public String toString()
    {       
        String result = new String();
        //Заполнение каждой строки

        for(int i = 0; i < mHeight; i++)
        {
            for(int j = 0; j < mColumns.length; j++)
            {
                //Сделать проверку выхода за пределы высоты столбика и если произошло заполнять пробелами
                String text = new String();
                if(i < mColumns[j].getFormatText().size())
                {
                    text = mColumns[j].getFormatText().get(i);
                    
                }
                else
                {
                    for(int f = 0; f < mColumns[j].getWidth(); f++)
                        text += " ";
                }
                if(j == 0)
                {
                    result += "| " + text;
                }
                else
                {
                    result += " | " + text;
                }
            }
            result += " |" + TransferSign.write();
        }

        return result;
    }
}
