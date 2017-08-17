package generator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Класс отчета, формирующегося из считанных параметров. 
 * Сохраняет себя в текстовом варианте по указанному пути.
 * @author avdrine
 */
public class Report {
    /** Динамический массив страниц */
    private ArrayList<Page> mPages;
    /** Максимальная высота страницы */
    private int mPageHeight;
    /** Максимальная ширина страницы*/
    private int mPageWidth;
    /** Динамический массив названий заголовков */
    private ArrayList<String> mHeadersNames;
    /** Динамический массив длин заголовков */
    private ArrayList<Integer> mColumnsWidths;
    /** Количество столбцов */
    private int mNumberOfColumns;
    /** Путь до файла, куда будет сгенерирован отчет */
    private final String mPathOfReport;
    
    /** 
     * Конструктор отчета
     * @param pathOfOptions Путь до параметров в файле XML
     * @param pathOfdata Путь до данных в формате TSV
     * @param pathOfReport Путь до файла генерируемого отчета
     */
    Report(String pathOfOptions, String pathOfdata, String pathOfReport) throws FileNotFoundException, IOException, Exception
    {
        mPathOfReport = pathOfReport;
        mHeadersNames = new ArrayList<String>();
        mColumnsWidths = new ArrayList<Integer>();
        parseXML(pathOfOptions);
        
        fillPages(textToData(readFileWithData(pathOfdata)));
    }
    
    /** 
     * Заполненить страницы содержимым
     * @param data Массив данных, которыми будет заполнены страницы
     */
    private void fillPages(ArrayList<Data> data) throws Exception
    {
        mPages = new ArrayList<Page>();
        int indexOfActiveData = 0;
        while(indexOfActiveData < data.size())
        {
            mPages.add(new Page(mPageHeight,mPageWidth));
            
            //Заполнение заголовка у новой страницы
            LinkedHashMap<String, Integer> headersColumns = new LinkedHashMap<String, Integer>();
            for(int i = 0; i < mNumberOfColumns; i++)
            {
                headersColumns.put(mHeadersNames.get(i), mColumnsWidths.get(i));
            }
            
            Field headersField = new Field(headersColumns);
            mPages.get(mPages.size()-1).addHeader(headersField);
            
            //Заполнение полей
            while(indexOfActiveData < data.size())
            {
                try
                {
                    LinkedHashMap<String, Integer> columns = new LinkedHashMap<String, Integer>();
                    for(int i = 0; i < mNumberOfColumns; i++)
                        columns.put(data.get(indexOfActiveData).get(i), mColumnsWidths.get(i));
                    
                    Field field = new Field(columns);
                    mPages.get(mPages.size()-1).addField(field);
                    indexOfActiveData++;
                }
                catch (Exception e)
                {
                    break;
                }
            }
        }
    }
    
    @Override
    /** 
     * Представление отчета в текстовом виде
     * @return оформленный текст отчета
     */
    public String toString()
    {
        String result = new String();
        for(int i = 0; i < mPages.size(); i++)
        {
            if(i != 0)
            {
                result += "~" + TransferSign.write();
            }
            result += mPages.get(i).toString();
        }
        return result;
    }
    
    /** 
     * Получение текста из файла
     * @param fileName Путь до файла
     * @return Содержимое файла
     */
    private String readFileWithData(String fileName) throws FileNotFoundException, IOException
    {        
        existsFile(fileName);
        
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), "UTF16"));
        StringBuilder lines = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.append(line);
            lines.append(TransferSign.write());
        }
        return lines.toString();
    }
    
    /** 
     * Проверка, существует ли файл
     * @param fileName Путь до файла
     */
    private void existsFile(String fileName) throws FileNotFoundException 
    {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }
    
    /** 
     * Парсинг текста для создания объектов класса Data
     * @param text Обрабатываемый текст
     * @return Массив объектов класса Data
     */
    private ArrayList<Data> textToData(String text) throws Exception
    {
        ArrayList<Data> data = new ArrayList<Data>();
        
        String[] lines = text.split(TransferSign.write());
        for(String i : lines)
        {
            String[] words = i.split("\t");
            if(words.length != mNumberOfColumns)
                throw new ArrayStoreException("Bad data in file");
            Data tmpData = new Data(Integer.parseInt(words[0]),words[1],words[2]);
            data.add(tmpData);
        }
        return data;
    }
    
    /** 
     * Парсинг XML-файла
     * @param filepath Путь до файла
     */
    private void parseXML(String filepath) throws Exception
    {
        //Получаем список узлов
        Document doc = getDocument(filepath);
        
        //Создаем структуры для хранения нодов нужной глубины
        NodeList parametersNodes = doc.getChildNodes();
        NodeList parametersNodes2 = parametersNodes.item(0).getChildNodes();
        NodeList[] parameters;
        parameters = new NodeList[parametersNodes2.getLength()];
        
        //Заполняем ноды
        for (int i = 0; i < parametersNodes2.getLength(); i++) 
        {
            parameters[i] = parametersNodes2.item(i).getChildNodes();
        }
        
        //Анализ значений
        for (NodeList parameter : parameters) {
            for (int j = 0; j < parameter.getLength(); j++) {
                switch (parameter.item(j).getNodeName()) 
                {
                    case "width":
                        mPageWidth = Integer.parseInt(parameter.item(j).getTextContent());
                        break;
                    case "height":
                        mPageHeight = Integer.parseInt(parameter.item(j).getTextContent());
                        break;
                    case "column":
                        for (int l = 0; l < parameter.item(j).getChildNodes().getLength(); l++) 
                        {
                            switch (parameter.item(j).getChildNodes().item(l).getNodeName()) {
                                case "title":
                                    mHeadersNames.add(parameter.item(j).getChildNodes().item(l).getTextContent());
                                    break;
                                case "width":
                                    int width = Integer.parseInt(parameter.item(j).getChildNodes().item(l).getTextContent());
                                    if(width < 4)
                                        throw new Exception("Not enough space to column");
                                    mColumnsWidths.add(width);
                                    break;
                                case "#text":
                                    continue;
                                default:
                                    throw new Exception("Bad structure of xml");
                            }
                        }
                        if(mColumnsWidths.size() != mHeadersNames.size() )
                            throw new Exception("Bad structure of xml");
                        break;
                }
            }
        }
        mNumberOfColumns = mHeadersNames.size();
        
        //Расчет совпадает ли заявленная ширина страницы рассчитываемой по ширине столбцов
        int needSize = 0;
        for(int i : mColumnsWidths)
            needSize += i;
        //увеличиваем расчитываемую ширину на ширину знаков оформления
        needSize += 4 + (mNumberOfColumns-1) * 3;
        if(needSize != mPageWidth)
            throw new Exception("Bad width of page! It must be " + needSize);
    }
    
    /** 
     * Получение объекта Document для последующего разбора на Nodes
     * @param filepath Путь до файла
     * @return Все Nodes из файла 
     */
    private static Document getDocument(String filepath) throws Exception 
    {
        try 
        {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(filepath));
        } 
        catch (IOException | ParserConfigurationException | SAXException e) 
        {
            String message = "XML parsing error!";
            throw new Exception(message);
        }
    }
    
    /** 
     * Сохранение файла отчета
     * @throws Exception Не получилось создать файл
     */
    public void saveToFile() throws Exception
    {
        try {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(mPathOfReport), "UTF16"))) {
                bw.write(toString());
                bw.flush();
            }
        } catch (IOException e) {
            throw new Exception("can't create file");
        } 
    }
    
}
