import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Log{

	String textName1 = "log.txt";
	BufferedWriter bout;
	FileOutputStream fos;
	OutputStreamWriter or;
	public Log(String name) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		or = new OutputStreamWriter(fos);
		bout = new BufferedWriter(or);
	}
	void close(){
		try {
			bout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void out(String s){
		try {
			bout.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void out(int s){
		try {
			bout.write(String.valueOf(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void outln(String s){
		try {
			bout.write(s);
			bout.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void outln(double s){
		try {
			bout.write(String.valueOf(s));
			bout.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void outln(int s){
		try {
			bout.write(String.valueOf(s));
			bout.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void newline(){
		try {
			bout.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}