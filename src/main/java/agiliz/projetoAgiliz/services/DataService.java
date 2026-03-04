package agiliz.projetoAgiliz.services;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class DataService {

    public boolean isNotQuintoDiaUtil(LocalDate data) {
        return !getQuintoDiaUtil(data).equals(data);
    }

    //deixei esses dois de baixos públicos no caso de reutilizar em outro canto

    public LocalDate getQuintoDiaUtil(LocalDate data) {
        LocalDate dataAtual = LocalDate.of(data.getYear(), data.getMonth(), 1);
        int diasUteis = 0;

        while(diasUteis < 5) {
            if(isDiaUtil(dataAtual)) diasUteis++;
            if(diasUteis < 5) dataAtual = dataAtual.plusDays(1);
        }

        return dataAtual;
    }

    // TODO procurar uma api pra pegar os feriados e considerar-los aqui
    public boolean isDiaUtil(LocalDate data) {
        return data.getDayOfWeek() != DayOfWeek.SATURDAY &&
                data.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
