import java.util.*;

public class MiniTeste {
    static Map<String, String> dicPredicado = new HashMap<String, String>();

    public void inicializaDic(){
        dicPredicado.put("nenhum", "¬");
        dicPredicado.put("é", "->");
        dicPredicado.put("com", "^");
        dicPredicado.put("há", "∃");
        dicPredicado.put("não", "¬");
        dicPredicado.put("todo", "∀");
        dicPredicado.put("que", "->");
        dicPredicado.put("e", "^");
        dicPredicado.put("existe", "∃");
        dicPredicado.put("existem", "∃");
        dicPredicado.put("ninguém", "¬");
        dicPredicado.put("ou", "v");
        dicPredicado.put("sou", "->");
        dicPredicado.put("se", "^");
        dicPredicado.put("qualquer","∀");
        dicPredicado.put("então","->");
    }

    public ArrayList<String> tradutorLP(String predicado) {
        inicializaDic();

        String[] predicadosList = predicado.split(" ");
        for (int i = 0; i < predicadosList.length; i++) {
            String atual = predicadosList[i].toLowerCase(Locale.ROOT);
            if (dicPredicado.containsKey(atual)) {
                predicadosList[i] = dicPredicado.get(atual);
            }
        }
        ArrayList<String> predicadoArray = new ArrayList<>(Arrays.asList(predicadosList));
        analisadorDuplicidade(predicadoArray);
        return analisadorLexico(predicadoArray);
    }

    public static void analisadorDuplicidade(ArrayList<String> predicado) {
        for (int i = 1; i < predicado.size(); i++) {
            if (predicado.get(i).equals(predicado.get(i - 1))) {
                predicado.remove(i);
            } else if (predicado.get(i - 1).equals("∃") && (predicado.get(i).equals("um") || (predicado.get(i).equals("uma")))) {
                predicado.remove(i);
            } else if (predicado.get(i - 1).equals("^") && (predicado.get(i).equals("um") || (predicado.get(i).equals("uma")))) {
                predicado.remove(i);
            } else if (predicado.get(i - 1).equals("∀") && (predicado.get(i).equals("mundo"))) {
                predicado.remove(i);
            }

        }
    }

    public static ArrayList<String> analisadorLexico(ArrayList<String> input) {
        ArrayList<String> funcao = new ArrayList<>();
        ArrayList<String> variavel = new ArrayList<>();
        ArrayList<String> simbolo = new ArrayList<>();
        ArrayList<String> saida = new ArrayList<>();

        //Primeiro input que n tá no dic é variavel
        if (!dicPredicado.containsValue(input.get(0))) {
            variavel.add(input.get(0));
            input.remove(0);
        }

        //remove os duplicados e "nada" e "de"
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i).equals(input.get(i - 1))) {
                input.remove(i);
            } else if (input.get(i).equals("nada")) {
                input.remove(i);
            } else if (input.get(i).equals("de")) {
                input.remove(i);
            } else if (input.get(i).equals("um") || (input.get(i).equals("uma"))){
                input.remove(i);
            } else if (input.get(i-1).equals("não") && input.get(i).equals("é")){
                input.remove(i);
            }
        }

        int k = 1;
        //Pega todas as funções de acordo com a ordem do input boy
        while(k < input.size()){
            //Verifica se o anterior ao atual é simbolo, se for, o atual é função
            if(dicPredicado.containsValue(input.get(k-1)) && !dicPredicado.containsValue(input.get(k))) {
                funcao.add(input.get(k));
            }
            k++;
        }

        int i = 0;
        //Pega todos os simbolos restantes
        while(i < input.size()){
            //Verifica se o atual é simbolo, se for, coloca no simbolos
            if (dicPredicado.containsValue(input.get(i))) {
                simbolo.add(input.get(i));
            }
            i++;
        }
        i = 1;
        //Pega todas as variáveis tlgd boy
        for (i = 1; i < input.size(); i++) {
            //Se o anterior for simbolo, eu sou função
            if (!simbolo.contains(input.get(i)) && !funcao.contains(input.get(i))) {
                if (funcao.contains(input.get(i - 1))) {
                    if(!variavel.contains(input.get(i))){
                        variavel.add(input.get(i));
                    }
                }
            }
            //Se o anterior for função, eu sou variável
            if (!simbolo.contains(input.get(i)) && !funcao.contains(input.get(i))) {
                if (variavel.contains(input.get(i - 1))) {
                    funcao.add(input.get(i));
                }
            }
        }

        //Se tiver imp depois de neg então troca
        for (i = 1; i < simbolo.size(); i++) {
            if (simbolo.get(i).equals("->") && simbolo.get(i - 1).equals("¬")) {
                simbolo.set(i, "¬");
                simbolo.set(i - 1, "->");
            }
        }

        if (!simbolo.get(0).equals("∀") && !simbolo.get(0).equals("∃")) {
            saida.add("∀x");
        } else if (simbolo.get(0).equals("∃")) {
            saida.add("∃x");
            simbolo.remove(0);
        } else if (simbolo.get(0).equals("∀")) {
            saida.add("∀x");
        }

        boolean flag = true;
        int iterador = 0;
        while (flag) {
            if (simbolo.isEmpty() && funcao.isEmpty() && variavel.isEmpty()) {
                flag = false;
            } else if (simbolo.size() == 1 && funcao.size() == 1 && variavel.size() == 1) {
                if (simbolo.get(0).equals("->")) {
                    saida.remove(0);
                    saida.add("∀" + variavel.get(0));
                    saida.add(funcao.get(iterador) + "(" + variavel.get(0) + ")");
                    simbolo.remove(0);
                    funcao.remove(0);
                    variavel.remove(0);
                }
            } else {
                if (!funcao.isEmpty()) {
                    if (!variavel.isEmpty()) {
                        saida.add(funcao.get(iterador) + "(x," + variavel.get(0) + ")");
                        variavel.remove(0);
                    } else {
                        saida.add(funcao.get(iterador) + ("(x)"));
                    }
                    funcao.remove(iterador);
                }
                if (!simbolo.isEmpty()) {
                    if (funcao.isEmpty()) {
                        String aux = saida.get(saida.size() - 1);
                        saida.remove(saida.size() - 1);
                        saida.add(simbolo.get(iterador));
                        simbolo.remove(iterador);
                        saida.add(aux);

                    } else {
                        saida.add(simbolo.get(iterador));
                        simbolo.remove(iterador);
                    }
                }
                // && funcao.isEmpty() PODEC q eu tire
                if (!variavel.isEmpty() && funcao.isEmpty()) {
                    saida.add(variavel.get(iterador));
                    variavel.remove(iterador);
                }
            }
        }
        for (i = 1; i < saida.size(); i++) {
            if (saida.get(i).equals("∃")) {
                saida.remove(i);
            }
            if (saida.get(i).equals("∀")) {
                saida.remove(i);
            }
        }
        /*
        System.out.println(simbolo+" Simbolo");
        System.out.println(funcao+" função");
        System.out.println(variavel+" variavel");
        System.out.println(saida);
         */
        analisadorDuplicidade(saida);
        return(saida);

    }
}
