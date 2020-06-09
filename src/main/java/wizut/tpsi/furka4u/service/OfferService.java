package wizut.tpsi.furka4u.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wizut.tpsi.furka4u.entity.*;
import wizut.tpsi.furka4u.model.OfferFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional
public class OfferService {

    @PersistenceContext
    private EntityManager em;

    public CarManufacturer getCarManufacturer(int id) {
        return em.find(CarManufacturer.class, id);
    }

    public CarModel getModel(int id) {
        return em.find(CarModel.class, id);
    }

    public Offer getOffer(int id) {
        return em.find(Offer.class, id);
    }

    public List<CarManufacturer> getCarManufactures() {
        return em.createQuery("select cm from CarManufacturer cm order by cm.name", CarManufacturer.class)
                .getResultList();
    }

    public List<CarModel> getCarModels() {
        return em.createQuery("select cm from CarModel cm order by cm.name", CarModel.class)
                .getResultList();
    }

    public List<CarModel> getCarModels(int manufacturerId) {
        return em.createQuery("select cm from CarModel cm where cm.manufacturer.id = :id order by cm.name", CarModel.class)
                .setParameter("id", manufacturerId)
                .getResultList();
    }

    public List<Offer> getOffers(int page, int size, String order, String dir) {
        return em.createQuery("select o from Offer o order by " + order + " " + dir, Offer.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long getOffersTotal() {
        return em.createQuery("select count(o) from Offer o", Long.class)
                .getSingleResult();
    }

    public List<Offer> getOffers(OfferFilter offerFilter, int page, int size, String order, String dir) {
        StringBuilder query = new StringBuilder("select o from Offer o where 1=1 ");

        if (offerFilter.getManufacturerId() != null) {
            query.append("and o.model.manufacturer.id = :manufacturerId ");
        }

        if (offerFilter.getModelId() != null) {
            query.append("and o.model.id = :modelId ");
        }

        if (offerFilter.getFuelTypeId() != null) {
            query.append("and o.fuelType.id = :fuelTypeId ");
        }

        if (offerFilter.getYearFrom() != null) {
            query.append("and o.year >= :yearFrom ");
        }

        if (offerFilter.getYearTo() != null) {
            query.append("and o.year <= :yearTo ");
        }

        if (offerFilter.getDescription() != null) {
            query.append("and o.description like :desc ");
        }

        query.append("order by ")
                .append(order)
                .append(" ")
                .append(dir);

        TypedQuery<Offer> typedQuery = em.createQuery(query.toString(), Offer.class);

        if (offerFilter.getManufacturerId() != null) {
            typedQuery.setParameter("manufacturerId", offerFilter.getManufacturerId());
        }

        if (offerFilter.getModelId() != null) {
            typedQuery.setParameter("modelId", offerFilter.getModelId());
        }

        if (offerFilter.getFuelTypeId() != null) {
            typedQuery.setParameter("fuelTypeId", offerFilter.getFuelTypeId());
        }

        if (offerFilter.getYearFrom() != null) {
            typedQuery.setParameter("yearFrom", offerFilter.getYearFrom());
        }

        if (offerFilter.getYearTo() != null) {
            typedQuery.setParameter("yearTo", offerFilter.getYearTo());
        }

        if (offerFilter.getDescription() != null) {
            typedQuery.setParameter("desc", "%" + offerFilter.getDescription() + "%");
        }

        return typedQuery
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Offer> getOffersByModel(int modelId) {
        return em.createQuery("select o from Offer o where o.model.id = :id", Offer.class)
                .setParameter("id", modelId)
                .getResultList();
    }

    public List<Offer> getOffersByManufacturer(int manufacturerId) {
        return em.createQuery("select o from Offer o where o.model.manufacturer.id = :id", Offer.class)
                .setParameter("id", manufacturerId)
                .getResultList();
    }

    public List<BodyStyle> getBodyStyles() {
        return em.createQuery("select bs from BodyStyle bs order by bs.name", BodyStyle.class)
                .getResultList();
    }

    public List<FuelType> getFuelTypes() {
        return em.createQuery("select ft from FuelType ft order by ft.name", FuelType.class)
                .getResultList();
    }

    public Offer createOffer(Offer offer, User user) {
        offer.setUser(user);

        em.persist(offer);
        return offer;
    }

    public Offer saveOffer(Offer offer) {
        return em.merge(offer);
    }

    public Offer deleteOffer(Integer id) {
        Offer offer = em.find(Offer.class, id);
        em.remove(offer);

        return offer;
    }
}
