package kennyboateng.Capstone_LensLobby.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.UnauthorizedException;
import kennyboateng.Capstone_LensLobby.services.FotografoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private FotografoService fotografoService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new UnauthorizedException("Per favore inserisci correttamente il token nell'Authorization Header");
//        }
//
//        String accessToken = authHeader.substring(7);
//
//        jwtTools.verifyToken(accessToken);
//
//        try {
//            Long fotografoId = jwtTools.getFotografoIdFromToken(accessToken);
//            Fotografo currentFotografo = fotografoService.findFotografoById(fotografoId);
//
//            if (currentFotografo != null) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        currentFotografo,
//                        null,
//                        currentFotografo.getAuthorities()
//                );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } else {
//                throw new UnauthorizedException("Utente non trovato.");
//            }
//        } catch (Exception e) {
//            throw new UnauthorizedException("Errore durante l'autenticazione: " + e.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Se il filtro non deve essere applicato, lascia passare la richiesta senza errori
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Per favore inserisci correttamente il token nell'Authorization Header");
            return;
        }

        String accessToken = authHeader.substring(7);

        jwtTools.verifyToken(accessToken);

        try {
            Long fotografoId = jwtTools.getFotografoIdFromToken(accessToken);
            Fotografo currentFotografo = fotografoService.findFotografoById(fotografoId);

            if (currentFotografo != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        currentFotografo,
                        null,
                        currentFotografo.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non trovato.");
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Errore durante l'autenticazione: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }


    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();
        AntPathMatcher matcher = new AntPathMatcher();

        // Accesso aperto per le rotte di autenticazione
        if (matcher.match("/authorization/**", path)) {
            return true;
        }

        // Accesso aperto per le immagini random (homepage)
        if (matcher.match("/immagini/random", path)) {
            return true;
        }

        // Accesso aperto per le richieste GET per le immagini
        if (matcher.match("/immagini/**", path) && method.equals("GET")) {
            return true;
        }

        // Accesso aperto per l'endpoint pubblico dei fotografi
        if (matcher.match("/fotografi/public/**", path)) {
            return true;
        }

        // Tutte le altre richieste richiedono autenticazione
        return false;
    }
    }


//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return new AntPathMatcher().match("/authorization/**", request.getServletPath());
//    }
//}
