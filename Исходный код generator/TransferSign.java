package generator;



/**
 * Класс для использования нужного знака переноса,
 * в зависимости от используемой системы
 * @author avdrine
 */
public class TransferSign {
    /** Знак переноса */
    static String mSign;
    static
    {
        if(isWindows())
            mSign = "\r\n";
        else
            mSign = "\n";
    }
    
    /** 
     * Возращает знак переноса в текстовом виде
     * @return Знак переноса
     */
    public static String write()
    {
        return mSign;
    }
    
    /**
     * Проверяет была ли запущена программа в ОС Windows
     * @return Запущена/не запущена в Windows
     */
    private static boolean isWindows(){

        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.indexOf( "win" ) >= 0); 

    }
}

