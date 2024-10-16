package kennyboateng.Capstone_LensLobby.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Fotografo fotografo) {
        // Genera un token per il fotografo
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Data di emissione del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))  // Imposta la scadenza del token a 7 giorni
                .setSubject(String.valueOf(fotografo.getId()))  // Imposta il subject del token con l'ID del fotografo
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))  // Firma il token usando la chiave segreta
                .compact();  // Costruisce il JWT e lo serializza in una stringa compatta
    }

    public void verifyToken(String token) {
        try {
            // Verifica la validità del token. Se il token è manipolato o scaduto, lancerà un'eccezione.
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))  // Imposta la chiave per la verifica della firma
                    .build()
                    .parseClaimsJws(token);  // Effettua il parsing del token JWS per verificare la sua autenticità e validità
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi col token! Per favore effettua di nuovo il login.");
        }
    }

    public Long getFotografoIdFromToken(String token) {
        // Estrae l'ID del fotografo dal token
        return Long.parseLong(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
