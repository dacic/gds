package application.model;

public class FormaterBarcode {
	public String formatCode(String eanBrut){
	 String formattee = " ";
     try {
         if (eanBrut.length() <= 8) {
             formattee = String.format("%08d", Long.parseLong(eanBrut));
             eanBrut = formattee;
         } else if (eanBrut.length() > 8 && eanBrut.length() <= 13 && eanBrut.length() != 12) {
             formattee = String.format("%013d", Long.parseLong(eanBrut));
             eanBrut = formattee;
         }
     } catch (NumberFormatException e) {
         //msg(invalid_ean);
     }
	return formattee;
	}

}
