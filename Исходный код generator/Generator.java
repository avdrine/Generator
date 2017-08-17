package generator;

/**
 * Класс, генерирующий из файла в формате tsv текстовый отчет,
 * с внешними настройками в файле формата xml
 * @author avdrine
 */
public class Generator {

    /**
     * @param args первый параметр - путь до файла с настройками xml; 
     * второй параметр - путь до файла с данным;,
     * третий параметр - путь, где будет храниться отчет.
     */
    public static void main(String[] args) {
        if(args.length < 3)
            System.out.println("Few arguments");
        else if(args.length > 3)
            System.out.println("Too many arguments");
        else
        {
            try
            {
                Report r = new Report(args[0],args[1],args[2]);
                r.saveToFile();
                System.out.println("Success");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
            
    }
    
}
