//package fr.eni.bookhub_back.security;
//
//import fr.eni.projeteniencheres.bo.Utilisateur;
//import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EncheresUserDetailsService implements UserDetailsService {
//
//    private final UtilisateurRepository utilisateurRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public EncheresUserDetailsService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//        this.utilisateurRepository = utilisateurRepository;
//    }
//
//    @Override
//    //cette méthode est appelée par spring à chaque fois q'un utilisateur essaie de se connecter
//    public UserDetails loadUserByUsername(String pseudoOuEmail) throws UsernameNotFoundException {
//
//        Utilisateur utilisateur = utilisateurRepository
//                .findUtilisateurByPseudoOrEmail(pseudoOuEmail, pseudoOuEmail)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("Utilisateur introuvable")
//                );
//        String role = utilisateur.isAdministrateur() ? "ADMIN" : "USER";
//        User.UserBuilder builder = User.withUsername(utilisateur.getPseudo())
//                .password(utilisateur.getMotDePasse())
//                .roles(role);
//
//        return builder.build();
//    }
//}
