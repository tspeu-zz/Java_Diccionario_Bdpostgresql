package controlador;



import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jm
 */
public class ConMannager {
//    public static final String driverPostG ="org.postgresql.Driver";
//    public static final String pathPostG="jdbc:postgresql://localhost:5432/postgres";
//    public static final String userPostG="postgres";
//    public static final String passPostG="qwerty";
    
//    public static final String driver ="com.mysql.jdbc.Driver";
//    public static final String path="jdbc:mysql://localhost/test";
//    public static final String user="root";
//    public static final String pass="qwerty";

    public  Connection con;
    public  Statement st;
    public  ResultSet rs,cont;
    public  PreparedStatement ps;    
    Integer vistoA;
    
    public   ConMannager(String driv,String ruta,String use,String pasw){    
    try {
       
        Class.forName(driv);
        con = DriverManager.getConnection(ruta,use,pasw);
        st = con.createStatement();
        System.out.println("coneccion Ok");
    } 
    catch (SQLException ex) {
        Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
    }
       
    }
    
//    public void exUpdate(String sql){
//        try {
//            st.executeUpdate(sql);            
//            System.out.println(sql);
//            st.close();           
//        }  
//        catch (SQLException ex) {
//            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
//    public void exQuery(String sql, int i) {
//        try {
//            rs = st.executeQuery(sql);
//            while(rs.next()){           
//            System.out.println(rs.getString(i));
// 
//            } 
//            rs.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
    
    public static ResultSet consultarPedidos(ConMannager con, String sql){
    
//    String sql="select idpedido,idarticulo,cantidad from pedido order by idpedido"; 
        try {
            con.rs = con.st.executeQuery(sql);
//            while(con.rs.next()){
//                
//        System.out.println(con.rs.getInt(1)+"---,----"+con.rs.getString("CANTIDAD"));    
//            
//            }           
        } catch (SQLException ex) {
            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
        }
      return  con.rs;   
    }
    
   /* ******MUETRA TODA ******/
    public DefaultTableModel mostrarTodoDatosBD(String sql) {      
        
        String columnas[]={"Palabra","Visto"};
        String datos[][]={};
        DefaultTableModel modelo = new DefaultTableModel(datos,columnas);     
        modelo.addColumn("idioma");
        modelo.addColumn("traduccion");
        System.out.println("= BD ==");

        try {
            rs = st.executeQuery(sql);
            while(rs.next()){           
//                vistoA = ((BigDecimal)  rs.getObject(2)).intValue();
//                BigDecimal vistoB = ((BigDecimal)  rs.getObject(2));
//                BigDecimal incrementa = new BigDecimal(1);
//                incrementa = incrementa.add(vistoB);
                int vistoVeces = rs.getInt(2) + 1;    
                //Valor2 = Valor2.add(Valor1);
                Array nuevo_array = rs.getArray(3);
                Object[] trads = (Object[]) nuevo_array.getArray();
                int valor = trads.length;
                for (int x=0;x<valor;x++){
                    oracle.sql.STRUCT obj1 = (oracle.sql.STRUCT)trads[x]; //sentencia sql de objetos
                    String idiom = (String) obj1.getAttributes()[0];//sacar atributos
                    String trad = (String) obj1.getAttributes()[1];                    
                    System.out.print(idiom +" - "+ trad +", ");
               //rs.getObject(3)       
                Object[] fila= {rs.getObject(1),vistoVeces ,idiom,trad};                   
                modelo.addRow(fila);
                } 
            } 
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
        }      
            return modelo;
}   
    
//    ps(sqlC,Statement.RETURN_GENERATED_KEYS); 
     public void PreparedStatement(String sql,int i) {
        //Connection con = conectar();
         //"INSERT INTO menudb(id,nombre)VALUES(?,?)"
    try {
        PreparedStatement pst = con.prepareStatement(sql,i);   
        pst.execute();

        } catch (SQLException ex) {
        Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
    
    
//    public void PreparedStatement2( String n,int i){
//        //Connection con = conectar();
//    try {
//        PreparedStatement pst = con.prepareStatement(n, i);
//        
//         pst.execute();
//
//        } catch (SQLException ex) {
//        Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
//    }
//        
//    }
    
    public static boolean exista(String termino, Connection cOracle) throws SQLException{
        String sqlC = "SELECT * FROM ejercicio WHERE (palabra = '"+termino+"')";
        PreparedStatement ps = cOracle.prepareStatement(sqlC,Statement.RETURN_GENERATED_KEYS);
        ResultSet cont = ps.executeQuery(sqlC);
        //System.out.println(sqlC);
        cont.next();
        if (cont.getRow()==0)
            return false;
        else
            return true;
    }        
    
    
    public static String leerCadena(String cadena, String mensaje, boolean repetirMientrasVacio) {
        
        cadena = null;
        do
        {
            if (cadena != null) {
                System.out.println("ERROR: En este caso, no es admisible un valor vacio o nulo.");
            }
            System.out.print(mensaje);
//            cadena ;
        }
        while (repetirMientrasVacio && (cadena == null || cadena.length() == 0));
        cadena = cadena.trim();
        if (cadena.isEmpty()) {
            cadena = null;
        }
        return cadena;
    }
    
    /****** comprueba que no este vacio  *****/
    public static String leerCadena(String mensaje, boolean repetirMientrasVacio) {
        
        String cadena = null;
        do
        {
            if (cadena != null) {
                System.out.println("ERROR: En este caso, no es admisible un valor vacio o nulo.");
            }
            System.out.print(mensaje);
            cadena = JOptionPane.showInputDialog(mensaje);
//            cadena = inScanner.nextLine();
        }
        while (repetirMientrasVacio && (cadena == null || cadena.length() == 0));
        cadena = cadena.trim();
        if (cadena.isEmpty()) {
            cadena = null;
        }
        return cadena;
    }
    
    public void insertarTerminoTraduccion(String termino, String idioma, String traduccion){
        try {         
//    String seguir="si";
            Hashtable lista = new Hashtable();
//           termino = UtilConsola.leerCadena("\nIntroduce un termino: ", true);
            while(exista(termino, con)){
                System.out.println("\nEse termino ya existe...");
                termino = JOptionPane.showInputDialog("Introduzca palabra");
//                                termino=UtilConsola.leerCadena("\nIntroduce otro termino: ", true);
            }
            idioma=JOptionPane.showInputDialog("Introduzca el idioma:");
//         String idioma=UtilConsola.leerCadena("Introduce el idioma: ", true);
            while (idioma!=null){
                traduccion=JOptionPane.showInputDialog("Introduzca la traducción:");
//            String traduccion=UtilConsola.leerCadena("Introduce la traducción: ", true);
                lista.put(idioma,traduccion); //em Lista            
//                if (traduccion.equals("")){
//                return;
//                }
//              idioma= this.leerCadena(JOptionPane.showInputDialog("Introduzca otro idioma:"), false);
                idioma= this.leerCadena("Introduce el idioma: [ENTER para salir] ", false);
                
            }
            Enumeration<String> id = lista.keys();
            sqlC="INSERT INTO ejercicio VALUES ('"+termino+"',0, tabla_traduccion (";
            while(id.hasMoreElements()){
                idioma = id.nextElement();
                sqlC=sqlC.concat("tipo_traduccion('");
                sqlC=sqlC.concat(idioma);
                sqlC=sqlC.concat("', '");
                sqlC=sqlC.concat((String) lista.get(idioma));
                if (id.hasMoreElements())
                    sqlC=sqlC.concat("'), ");
                else
                    sqlC=sqlC.concat("')))");
            }
         
                //System.out.println("\n"+sqlC+"\n");    //Control de cadena
                ps=con.prepareStatement(sqlC,Statement.RETURN_GENERATED_KEYS);
             
            ps.executeUpdate();

            
        } catch (SQLException ex) {
            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public DefaultTableModel BuscaryMostrar(){
        String columnas[]={"PalabraBusca","VistoBusca"};
        String datos[][]={};
        DefaultTableModel modelo = new DefaultTableModel(datos,columnas);     
        modelo.addColumn("idiomaBuscar");
        modelo.addColumn("traduccionBusca");
        System.out.println("= BD ==");
//         terminoBuscar=buscar;
        terminoBuscar =ConMannager.leerCadena("\nIntroduce el termino a buscar: ", false);
//        if(terminoBuscar.equals("")){
//            JOptionPane.showMessageDialog(null, "El campo esta vacio");        
//           }
//        else{    
            String sqlCBusca="SELECT * FROM ejercicio WHERE (palabra LIKE '%"+terminoBuscar+"%')";
        try {
             ps=con.prepareStatement(sqlCBusca,Statement.RETURN_GENERATED_KEYS);        
             cont = ps.executeQuery(sqlCBusca);
                while (cont.next()){
                    System.out.print("\nTermino: " + cont.getString(1) +  "   Traducciones: "); //est
                    int visto = cont.getInt(2) + 1;
                mi_array = cont.getArray(3);
                    Object[] trads = (Object[]) mi_array.getArray();
                    int valor = trads.length;
                    for (int x=0;x<valor;x++){
                        oracle.sql.STRUCT obj1 = (oracle.sql.STRUCT)trads[x];
                        idi = (String) obj1.getAttributes()[0];
                        tra = (String) obj1.getAttributes()[1];                    
                        System.out.print(idi +" - "+ tra +", "); //test
                        
                        Object[] filaNueva= { cont.getString(1),visto ,idi,tra};
                        modelo.addRow(filaNueva);
                    }
                    System.out.print("visualizado "+visto+" veces");
                }
                cont.close(); 
                ps.close();
     
        }   catch (SQLException ex) {
            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
        }
//    }        
        return modelo;
}
    
    public void Borrar(String borra){
     
//     Eliminar un determinado termino con sus traducciones
//        termino=this.leerCadena("\nIntroduce el termino a borrar: ", true);
        if (borra.equals("")){
         JOptionPane.showMessageDialog(null, "El campo no puede estar vacio");
        }else{   
        sqlC="DELETE FROM ejercicio WHERE (palabra = '"+borra+"')";
       
        try {
            ps=con.prepareStatement(sqlC,Statement.RETURN_GENERATED_KEYS);
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(ConMannager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
     
     
     }    
   
    
        Array mi_array;
        public String terminoBuscar;
        String sqlC, idi, tra;

   
    
}
