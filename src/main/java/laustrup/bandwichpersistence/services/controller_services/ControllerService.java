package laustrup.bandwichpersistence.services.controller_services;

import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.utilities.Liszt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerService<E> {

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param element The E element that is either null or not and should be returned.
     * @return The created ResponseEntity of an E element.
     */
    protected ResponseEntity<E> entityContent(E element) {
        if (element != null) return new ResponseEntity<>(element, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param elements The E elements that is either null or not and should be returned.
     * @return The created ResponseEntity of E elements.
     */
    protected ResponseEntity<Liszt<E>> entityContent(Liszt<E> elements) {
        if (elements != null) return new ResponseEntity<>(elements, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Will create a ResponseEntity with status of whether the content is null or not.
     * @param search The Search that is either null or not and should be returned.
     * @return The created ResponseEntity of a Search.
     */
    protected ResponseEntity<Search> searchContent(Search search) {
        if (search != null) return new ResponseEntity<>(search, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
