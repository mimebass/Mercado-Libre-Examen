package com.sistema.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sistema.dto.Adn;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AdnUtil {
	
	static String a = "AAAA";
    static String t = "TTTT";
    static String c = "CCCC";
    static String g = "GGGG";
    static List<String> adnList;

    //Convertir String de JSON en un array de Strings.
    public Adn parseJson(String cadenaADN)
    {
        try
        {
            ObjectMapper oMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return oMapper.readValue(cadenaADN, Adn.class);
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    //Verificar que la matriz ingresada cumpla con ciertos requisitos básicos antes de verificar si es mutante o no
    public boolean verifyMatriz(Adn adn)
    {
        return rangoMinimo(adn) && verifyCharacters(adn) && matrizCuadrada(adn);
    }

    //Verificar que sea una matriz con la que se pueda trabajar
    private boolean rangoMinimo( Adn adn) {
        //Si la matriz no es de al menos 4x4 no se puede analizar
        return adn.getDna().size() > 3;
    }

    //Verificar que sea una matriz de NxN
    private boolean matrizCuadrada(Adn adn) {
        //Si la cantidad de cadenas es diferente al largo de las cadenas entonces no es una matriz cuadrada
        int filas = adn.getDna().size();
        for(String s : adn.getDna())
        {
            if(s.length()!= filas)
            {
                return false;
            }
        }
        return true;
    }

    //Verificar que se hayan ingresado solo caracteres permitidos
    private boolean verifyCharacters( Adn adn) {
        //Si se ingresa un caracter diferente a los permitidos (A, T, C y G)
        for(String s : adn.getDna())
        {
            if(!s.matches("[ATCG]+"))
            {
                return false;
            }
        }
        return true;
    }
    
    private static boolean validation(String aux) {
        return aux.equals(a) || aux.equals(t) || aux.equals(c) || aux.equals(g);
    }

    private static boolean diagonal(int i, int j) {
        String aux = "";
        aux += adnList.get(i).charAt(j);;
        aux += adnList.get(i+1).charAt(j + 1);
        aux += adnList.get(i+2).charAt(j + 2);
        aux += adnList.get(i+3).charAt(j + 3);

        return validation(aux);
    }

    private static boolean vertical(int i, int j) {
        String aux = "";
        aux += adnList.get(i).charAt(j);;
        aux += adnList.get(i+1).charAt(j);
        aux += adnList.get(i+2).charAt(j);
        aux += adnList.get(i+3).charAt(j);

        return validation(aux);
    }

    private static boolean horizontal(int i, int j) {
        String aux = "";
        aux += adnList.get(i).charAt(j);;
        aux += adnList.get(i).charAt(j + 1);
        aux += adnList.get(i).charAt(j + 2);
        aux += adnList.get(i).charAt(j + 3);

        return validation(aux);
    }

    public static boolean esMutante(Adn adn) {
    	adnList = adn.getDna();
        int count = 0;
        int n = adnList.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < n - 3) {
                    if (j < n - 3) {
                        if (diagonal(i, j)) {
                            count++;
                        }
                    }
                    if (vertical(i, j)) {
                        count++;
                    }
                }

                if (j < n - 3) {
                    if (horizontal(i, j)) {
                        count++;
                    }
                }
                //Verifico si ya hay dos repeticiones
                if (count >= 2) {
                    return true;
                }
            }
        }
        return false;
    }
    
}

