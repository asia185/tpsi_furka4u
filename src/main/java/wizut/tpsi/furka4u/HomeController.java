package wizut.tpsi.furka4u;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import wizut.tpsi.furka4u.entity.*;
import wizut.tpsi.furka4u.exception.HttpAccessDenied;
import wizut.tpsi.furka4u.model.OfferFilter;
import wizut.tpsi.furka4u.model.OfferSorter;
import wizut.tpsi.furka4u.model.UserSession;
import wizut.tpsi.furka4u.service.OfferService;
import wizut.tpsi.furka4u.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    @GetMapping({"/", "/{page}/{order}/{dir}"})
    public String homeAction(Model model,
                             OfferFilter offerFilter,
                             OfferSorter offerSorter,
                             @PathVariable Optional<Integer> page,
                             @PathVariable Optional<String> order,
                             @PathVariable Optional<String> dir) {

        long total = offerService.getOffersTotal();

        int pageVal = page.orElse(1) - 1;
        int size = 20;

        String orderVal = order.orElse("title");
        String dirVal = dir.orElse("asc");

        if (offerSorter.getOrder() != null) {
            orderVal = offerSorter.getOrder();
        }

        if (offerSorter.getDir() != null) {
            dirVal = offerSorter.getDir();
        }

        List<CarManufacturer> carManufacturers = offerService.getCarManufactures();
        List<CarModel> carModels = Collections.emptyList();
        List<FuelType> fuelTypes = offerService.getFuelTypes();
        List<Offer> offers = offerService.getOffers(pageVal, size, orderVal, dirVal);

        if (offerFilter.getManufacturerId() != null
            || offerFilter.getModelId() != null
            || offerFilter.getFuelTypeId() != null
            || offerFilter.getYearFrom() != null
            || offerFilter.getYearTo() != null
            || offerFilter.getDescription() != null) {

            offers = offerService.getOffers(offerFilter, pageVal, size, orderVal, dirVal);
            total = offers.size();
        }

        if (offerFilter.getManufacturerId() != null) {
            carModels = offerService.getCarModels(offerFilter.getManufacturerId());
        }

        model.addAttribute("carManufacturers", carManufacturers);
        model.addAttribute("carModels", carModels);
        model.addAttribute("fuelTypes", fuelTypes);
        model.addAttribute("offers", offers);
        model.addAttribute("page", pageVal + 1);
        model.addAttribute("size", size);
        model.addAttribute("order", orderVal);
        model.addAttribute("dir", dirVal);
        model.addAttribute("total", total);

        return "index";
    }

    @GetMapping("/offer/{id}")
    public String detailsAction(Model model, @PathVariable Integer id) {
        model.addAttribute("offer", offerService.getOffer(id));
        return "offerDetails";
    }

    @GetMapping("/newoffer")
    public String newOfferAction(Model model, Offer offer) {
        if (userSession.getId() == null) {
            throw new HttpAccessDenied();
        }

        List<CarManufacturer> manufacturers = offerService.getCarManufactures();
        List<BodyStyle> bodyStyles = offerService.getBodyStyles();
        List<FuelType> fuelTypes = offerService.getFuelTypes();

        model.addAttribute("bodyStyles", bodyStyles);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("fuelTypes", fuelTypes);
        model.addAttribute("header", "Nowe ogłoszenie");
        model.addAttribute("action", "/newoffer");

        return "offerForm";
    }

    @PostMapping("/newoffer")
    public String createOfferAction(Model model, @Valid Offer offer, BindingResult binding) {
        if (userSession.getId() == null) {
            throw new HttpAccessDenied();
        }

        if (binding.hasErrors()) {
            return newOfferAction(model, offer);
         }

        User current = userService.getUserById(userSession.getId());

        offer = offerService.createOffer(offer, current);
        return "redirect:/offer/" + offer.getId();
    }

    @GetMapping("/editoffer/{id}")
    public String editOfferAction(Model model, @PathVariable Integer id) {
        if (userSession.getId() == null) {
            throw new HttpAccessDenied();
        }

        List<CarModel> models = offerService.getCarModels();
        List<BodyStyle> bodyStyles = offerService.getBodyStyles();
        List<FuelType> fuelTypes = offerService.getFuelTypes();

        model.addAttribute("models", models);
        model.addAttribute("bodyStyles", bodyStyles);
        model.addAttribute("fuelTypes", fuelTypes);
        model.addAttribute("header", "Edycja ogłoszenia");
        model.addAttribute("action", "/editoffer/" + id);
        model.addAttribute("offer", offerService.getOffer(id));

        return "offerForm";
    }

    @PostMapping("/editoffer/{id}")
    public String saveOfferAction(Model model, @PathVariable Integer id, @Valid Offer offer, BindingResult binding) {
        if (userSession.getId() == null) {
            throw new HttpAccessDenied();
        }

        User current = userService.getUserById(userSession.getId());

        if (!current.getId().equals(offer.getUser().getId())) {
            throw new HttpAccessDenied();
        }

        if (binding.hasErrors()) {
            return editOfferAction(model, id);
        }

        offer = offerService.saveOffer(offer);
        return "redirect:/offer/" + offer.getId();
    }

    @GetMapping("/deleteoffer/{id}")
    public String deleteOfferAction(Model model, @PathVariable Integer id) {
        if (userSession.getId() == null) {
            throw new HttpAccessDenied();
        }

        User current = userService.getUserById(userSession.getId());

        if (!current.getId().equals(offerService.getOffer(id).getUser().getId())) {
            throw new HttpAccessDenied();
        }

        Offer offer = offerService.deleteOffer(id);

        model.addAttribute("offer", offer);
        return "deleteOffer";
    }

    @GetMapping("/models/{manufacturerId}")
    public ResponseEntity<List<CarModel>> getModelsAction(@PathVariable Integer manufacturerId) {
        return ResponseEntity.ok(offerService.getCarModels(manufacturerId));
    }
}
