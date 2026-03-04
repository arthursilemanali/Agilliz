package agiliz.projetoAgiliz.services;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

@Service
@Slf4j
public class FirebaseService {
    @Autowired
    private ColaboradorService colaboradorService;

    public void notificarMotoboy(String idColaborador, String mensagem, String title) {
        var colaborador = colaboradorService.getPorId(UUID.fromString(idColaborador));

        try {
            Message message = Message.builder()
                    .setToken(colaborador.getFcm_token())
                    .putData("title", title)
                    .putData("body", mensagem)
                    .build();
            log.info("[FirebaseService.notificarMotoboy], mensagem {}", mensagem);
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.out.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}