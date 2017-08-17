package generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;

/**
 * Класс колонки, хранящий строку в виде массива строк ограниченной длины
 * @author avdrine
 */
public class Column {
    /** Текст в формате строчки */
    private String mText;
    /** Максимальная ширина строки */
    private int mWidth;
    /** Текст в формате нескольких строк */
    private ArrayList<String> mLines;
    
    /** 
     * Конструктор
     * @param text Текст, который будет обработан
     * @param width Ширина строки
     */
    Column(String text, int width)
    {
        mText = text;
        mWidth = width;
        formatText();
    }

    /** 
     * Измнение обрабатываемого текста
     * @param text Обрабатываемый текст
     */
    public void setText(String text)
    {
        formatText();
    }

    /** 
     * Изменение ширины строки
     * @param width Ширина строки
     */
    public void setWidth(int width)
    {
        mWidth = width;
        formatText();
    }
  
    /** 
     * Получение значения ширины
     * @return Ширина строк
     */  
    public int getWidth()
    {
        return mWidth;
    }

    /** 
     * Получение обрабатываемого текста
     * @return Обрабатываемый текст
     */
    public String getText()
    {
        return mText;
    }
    
    /** 
     * Получение обработанного текста
     * @return Обработанный текст
     */
    public ArrayList<String> getFormatText()
    {
        return mLines;
    }
    
    /** 
     * Получение количества строк в обработанном тексте
     * @return Количество строк
     */
    public int getHeight()
    {
        return mLines.size();
    }
    
    /** 
     * Заполнение остатка строки пробелами до кратности максимальной длины
     * @param text Обрабатываемый текст
     * @return Обработанный текст
     */
    private String fillingText(String text)
    {
        int remnant = mWidth - (text.length() % mWidth);
        //проверка, что количество букв в строке не кратно длине
        if(remnant != mWidth)
        {
            for(int i = 0; i < remnant; i++)
            {
                text += " ";
            }
        }

        return text;
    }
    
    /** 
     * Заполнение массива строк
     * @param text Обрабатываемый текст
     */
    private void fillingLines(String text)
    {
        String[] words = text.split(" ");
        int activeIndexOfWord = -1;
        String activeLine;
        String activeWord;
        String overage = new String();
        //Прогон каждой строчки, но по условиям окончания остаток слов
        while(activeIndexOfWord < words.length)
        {
            activeLine = new String();
            activeWord = new String();
            //Фрагмента слова для переноса нету
            if(overage.equals(""))
            {
                //Инкремент индекса активного слова для чтения нового слова
                activeIndexOfWord++;

                //Следующее слово есть
                if(activeIndexOfWord < words.length)
                {
                    //Считывание активного слова
                    activeWord = words[activeIndexOfWord];
                    //Вмещается ли активное слово в ширину
                    if(activeWord.length() <= mWidth)   //Вмещается
                    {
                        //Добавить активное слово в строку
                        activeLine += activeWord;

                        //Процесс добавления слов в строку, пока это возможно
                        while(activeIndexOfWord + 1 < words.length)
                        {
                            //Инкремент индекса активного слова
                            activeIndexOfWord++;
                            
                            //Считывание активного слова
                            activeWord = words[activeIndexOfWord];

                            //Расчет оставшегося места в строке
                            int freeSpace = mWidth - activeLine.length();
                            //Вмещается ли слово в оставшуюся ширину?
                            if(freeSpace - (activeWord.length() + 1) >= 0)
                                //Вмещается
                            {
                                //Добавление пробела в строку
                                activeLine += " ";

                                //Добавление слова в строку
                                activeLine += activeWord;

                                //Считанное слово было последним?
                                if(activeIndexOfWord + 1 >= words.length) 
                                    break;
                            }
                            else        //Не вмещается
                            {
                                if(haveSeparator(activeWord))
                                {
                                    int indexOfTransfer = indexOfTransfer(freeSpace-1, activeWord);
                                    //Сохранить обрезанную часть в фрагмент на перенос
                                    overage = activeWord.substring(indexOfTransfer, activeWord.length());
                                    //Обрезать слово
                                    activeWord = activeWord.substring(0, indexOfTransfer);
                                    
                                    //Добавить пробел
                                    activeLine += " ";
                                    //Добавить получившееся слово в строку
                                    activeLine += activeWord;
                                }
                                else
                                    //Декремент индекса активного слова
                                    activeIndexOfWord--;

                                break;
                            }
                        }
                    }
                    else                                //Не вмещается
                    {
                        if(haveSeparator(activeWord))
                        {
                            int indexOfTransfer = indexOfTransfer(mWidth, activeWord);
                            //Сохранить обрезанную часть в фрагмент на перенос
                            overage = activeWord.substring(indexOfTransfer, activeWord.length());
                            //Обрезать слово
                            activeWord = activeWord.substring(0, indexOfTransfer);
                            //Добавить получившееся слово в строку
                            activeLine += activeWord;
                        }
                        else
                        {
                            int breakPoint;
                            if(mWidth - activeWord.length() == -1)
                                breakPoint = mWidth-1;
                            else
                                breakPoint = mWidth;
                            //Сохранить обрезанную часть в фрагмент на перенос
                            overage = activeWord.substring(breakPoint, activeWord.length());
                            //Обрезать слово
                            activeWord = activeWord.substring(0, breakPoint);
                            //Добавить получившееся слово в строку
                            activeLine += activeWord;
                        }
                    }
                }
                //Следующего слова нету
                else
                    break;
            }
            //Фрагмент слова для переноса есть
            else
            {
                //Вмещается ли фрагмент слова в ширину строки
                if(overage.length() <= mWidth)  //Фрагмент слова вмещается
                {
                    //Добавить фрагмент слова для переноса в строку
                    activeLine += overage;

                    //Обнуление переменной с фрагментом для переноса
                    overage = new String();
                    //Остаток свободного места в строке больше 2?
                    if(mWidth - activeLine.length() > 2) //остаток больше 2
                    {
                        //Процесс добавления слов в строку, пока это возможно
                        while(activeIndexOfWord + 1 < words.length)
                        {
                            //Инкремент индекса активного слова
                            activeIndexOfWord++;

                            //Считывание активного слова
                            activeWord = words[activeIndexOfWord];

                            //Расчет оставшегося места в строке
                            int freeSpace = mWidth - activeLine.length();
                            //Вмещается ли слово в оставшуюся ширину?
                            if(freeSpace - (activeWord.length() + 1) >= 0)
                                //Вмещается
                            {
                                //Добавление пробела в строку
                                activeLine += " ";

                                //Добавление слова в строку
                                activeLine += activeWord;

                                //Считанное слово было последним?
                                if(activeIndexOfWord + 1 >= words.length) 
                                    break;
                            }
                            else        //Не вмещается
                            {
                                if(haveSeparator(activeWord))
                                {
                                    int indexOfTransfer = indexOfTransfer(freeSpace-1, activeWord);
                                    //Сохранить обрезанную часть в фрагмент на перенос
                                    overage = activeWord.substring(indexOfTransfer, activeWord.length());
                                    //Обрезать слово
                                    activeWord = activeWord.substring(0, indexOfTransfer);
                                    
                                    //Добавить пробел
                                    activeLine += " ";
                                    //Добавить получившееся слово в строку
                                    activeLine += activeWord;
                                }
                                else
                                    //Декремент индекса активного слова
                                    activeIndexOfWord--;
                                break;
                            }
                        }
                    }
                }
                else        //Фрагмент слова не вмещается в ширину
                {
                    String oldOverage = new String(overage);

                    if(haveSeparator(oldOverage))
                    {
                        int indexOfTransfer = indexOfTransfer(mWidth, oldOverage);
                        //Сохранить обрезанную часть в фрагмент на перенос
                        overage = oldOverage.substring(indexOfTransfer, oldOverage.length());
                        //Обрезать слово
                        oldOverage = oldOverage.substring(0, indexOfTransfer);
                    }
                    else
                    {
                        int breakPoint;
                        if(mWidth - oldOverage.length() == -1)
                            breakPoint = mWidth-1;
                        else
                            breakPoint = mWidth;
                        //Сохранить обрезанную часть в фрагмент на перенос
                        overage = oldOverage.substring(breakPoint, oldOverage.length());
                        //Обрезать слово
                        oldOverage = oldOverage.substring(0, breakPoint);
                    }
                    //Добавить получившееся слово в строку
                    activeLine += oldOverage;
                        
                }
            }
            activeLine = fillingText(activeLine);
            mLines.add(activeLine);
        }
    }
    
    /** 
     * Оформить текст
     */
    private void formatText()
    {
        mLines = new ArrayList<String>();
        if(mText.length() > mWidth)
        {
            fillingLines(mText);
        }
        else
        {
            mText = fillingText(mText);
            mLines.add(mText);
        }
    }
    
    /** 
     * Функция, рассчитывающая индекс для разбивки слова с разделителем
     * @param freeSpace Свободное место в строке
     * @param fullWord Слово для обработки
     * @return Индекс с которого стоит разбивать слово
     */
    private int indexOfTransfer(int freeSpace, String fullWord)
    {
        int[] indexesOfSubWords = indexesOfSubWords(fullWord);
        int[] indexesOfSeparators = indexesOfSeparators(fullWord);
        int[] allIndexes = new int[indexesOfSubWords.length + indexesOfSeparators.length];
        
        System.arraycopy(indexesOfSubWords, 0, allIndexes, 0, indexesOfSubWords.length);
        System.arraycopy(indexesOfSeparators, 0, allIndexes, indexesOfSubWords.length, indexesOfSeparators.length);
        
        Arrays.sort(allIndexes);
        
        for(int i = 0; i < allIndexes.length; i++)
        {
            if(allIndexes[i] > freeSpace)
            {
                if( i == 1)
                    return freeSpace;
                else
                    return allIndexes[i-1];
            }
                
        }
        return allIndexes[allIndexes.length - 1];
    }
    
    /** 
     * Расчет индексов начала слов, разделенных символами разделителями
     * @param fullWord Полное слово
     * @return Массив индексов, где начинаются подслова
     */
    private int[] indexesOfSubWords(String fullWord)
    {
        String[] subWords = fullWord.split("[^0-9a-zA-Zа-яА-Я]+");
        
        //Удаление пустых строк
        ArrayList<String> tmp = new ArrayList<String>();
        for(String i : subWords)
        {
            if(!i.equals(""))
                tmp.add(i);
        }
        subWords = new String[tmp.size()];
        subWords = tmp.toArray(subWords);
        
        int[] indexes = new int[subWords.length];
        for(int i = 0; i < subWords.length; i++)
        {
            if(i == 0)
                indexes[i] = fullWord.indexOf(subWords[i]);
            else
            {
                int lastIndex;
                lastIndex = indexes[i-1];
                indexes[i] = fullWord.indexOf(subWords[i], lastIndex+1);
            }
        }
        
        return indexes;
    }
    
    /** 
     * Расчет индексов начала разделителей
     * @param fullWordПолное слово
     * @return Массив индексов, где начинаются символы разделители
     */
    private int[] indexesOfSeparators(String fullWord)
    {
        
        String[] separators = fullWord.split("[0-9a-zA-Zа-яА-Я]+");
        
        
        //Удаление пустых строк
        ArrayList<String> tmp = new ArrayList<String>();
        for(String i : separators)
        {
            if(!i.equals(""))
                tmp.add(i);
        }
        separators = new String[tmp.size()];
        separators = tmp.toArray(separators);
        
        int[] indexes = new int[separators.length];
        for(int i = 0; i < separators.length; i++)
        {
            if(i == 0)
                indexes[i] = fullWord.indexOf(separators[i]);
            else
            {
                int lastIndex;
                lastIndex = indexes[i-1];
                indexes[i] = fullWord.indexOf(separators[i], lastIndex+1);
            }   
        }
        return indexes;
    }
    
    /** 
     * Проверка, есть ли в слове символы-разделители
     * @param word Слово для обработки
     * @return Есть или нет символы-разделители
     */
    private boolean haveSeparator(String word)
    {
        Pattern pattern = Pattern.compile("[^0-9a-zA-Zа-яА-Я]+");
        Matcher matcher = pattern.matcher(word);
        if(matcher.find())
            return true;
        else
            return false;
        
    }
}
