package controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;


/**
*
* @author Manuel
*/

@Named(value="languageController")
@SessionScoped
public class LanguageController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String locale;
	
    private static Map<String,Object> countries;
       static {
      
       countries = new LinkedHashMap<String,Object>();
       countries.put("Deutsch", Locale.GERMAN);
       countries.put("English", Locale.ENGLISH);
       
    }

    public Map<String, Object> getCountries() {
       return countries;
    }

    public String getLocale() {
       return locale;
    }

    public void setLocale(String locale) {
       this.locale = locale;
    }

	 /**
	  * Event aufrufen für die Sprache wechseln
	 * @param e
	 */
	public void localeChanged(ValueChangeEvent e) {
      String newLocaleValue = e.getNewValue().toString();
      
      for (Map.Entry<String, Object> entry : countries.entrySet()) {
         
         if(entry.getValue().toString().equals(newLocaleValue)) {
            FacesContext.getCurrentInstance()
               .getViewRoot().setLocale((Locale)entry.getValue());         
         }
      }
   }

}
