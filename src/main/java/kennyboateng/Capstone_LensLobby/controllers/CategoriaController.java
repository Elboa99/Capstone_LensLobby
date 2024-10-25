package kennyboateng.Capstone_LensLobby.controllers;

import kennyboateng.Capstone_LensLobby.enums.Categoria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorie")
public class CategoriaController {

    @GetMapping
    public List<String> getCategorie() {
        return Arrays.stream(Categoria.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
