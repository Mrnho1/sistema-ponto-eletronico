package dio.portifolio.util;

import dio.portifolio.entity.RegistroPonto;
import dio.portifolio.entity.TipoRegistro;

import java.time.Duration;
import java.util.List;

public class RegistroPontoUtils {

    public static long calcularMinutosTrabalhados(List<RegistroPonto> registros) {

        long total = 0;

        for (int i = 0; i < registros.size() - 1; i++) {

            RegistroPonto atual = registros.get(i);
            RegistroPonto proximo = registros.get(i + 1);

            if (atual.getTipo() == TipoRegistro.ENTRADA &&
                    proximo.getTipo() == TipoRegistro.SAIDA) {

                total += Duration
                        .between(atual.getDataHora(), proximo.getDataHora())
                        .toMinutes();
            }
        }

        return total;
    }
}