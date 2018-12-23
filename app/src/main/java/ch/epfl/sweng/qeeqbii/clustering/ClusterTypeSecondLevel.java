package ch.epfl.sweng.qeeqbii.clustering;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;

/**
 * Created by guillaume on 23/11/17.
 * Second Level of Clustering
 */

public enum ClusterTypeSecondLevel implements ClusterType, Serializable {
    // First level: CHOCOLAT
    BONBONS_CHEWING_GUM,
    CHOCOLAT,
    SAIN_BIO,
    BISCUITS_GAUFRES,
    DIVERS_CHOCOLAT,
    SPECIALITES,
    BLEVITA,
    JOURS_DE_FETES_EVENEMENTS,
    BISCUITS_POUR_ENFANTS,
    BISCUITS_POUR_ALLERGIQUES,
    DIVERS,

    CACAO_CHOCOLATS_EN_POUDRE,
    CONFITURES_PORTIONS_DE_MIEL,
    CONFITURES,
    MIEL,
    PATES_A_TARTINER,

    BOISSONS_ENERGETIQUES,
    THE,
    SIROP_SODA,
    CAFE,
    THE_GLACE,
    BOISSONS_SUCREES,
    JUS_DE_FRUITS_DE_LEGUMES,
    EAU_AROMATISEE,
    BOISSONS_DAPERITIF,
    EAU_MINERALE,
    BOISSONS_FAIBLES_EN_CALORIES,

    TOMATES_EN_CONSERVE,
    DU_MONDE_ENTIER,
    SOUPES_SAUCES_BOUILLON,
    PATES,
    LEGUMES_A_SALADE,
    LEGUMES_LEGUMES_AU_VINAIGRE,
    EPICES_HERBES,
    GARNITURES_MOULUES_CEREALES,
    CHAMPIGNONS_OLIVES_EN_CONSERVE,
    ANTIPASTI,
    VIANDE_EN_CONSERVE,
    POMMES_DE_TERRE_SPAETZLI,
    INGREDIENTS_DE_CUISSON,
    POISSON_EN_CONSERVE,
    GARNITURES_BIO,
    CHAMPIGNONS,
    RIZ,
    FRUITS_EN_CONSERVE,
    LEGUMINEUSES_HARICOTS_VERTS_SECHES,
    LEGUMINEUSES_FARCE_POUR_VOL_AU_VENT,
    DIVERS_GARNITURES,

    LEGUMES,
    FRUITS,

    VOLAILLE,
    SAUCISSES,
    VIANDE_SECHEE_JAMBON,
    SALAMI_LARD,
    PRODUITS_DE_CHARCUTERIE,
    PORC,
    BOEUF,
    LAPIN,
    VEAU,
    AGNEAU_CHEVRE,
    GIBIER,
    BIO,
    SOUS_PRODUITS,
    ACCOMPAGNEMENTS,
    CHEVAL,
    DIVERS_VIANDES,

    FARINE,
    PRODUITS_FRAIS_DE_BOULANGERIE_NON_REFRIGERES,
    INGREDIENTS_DE_BOULANGERIE,
    BOULANGERIE_PATISSERIE,
    PRODUITS_FRAIS_DE_BOULANGERIE_REFRIGERES,
    PETITS_PAINS,
    PAINS_DE_MIE,
    PAINS,
    PRODUITS_SANS_GLUTEN,
    PAINS_LONGUE_CONSERVATION,
    PAINS_BIO_LONGUE_CONSERVATION,
    FARINES_SANS_GLUTEN,
    BISCOTTES_PAIN_CROUSTILLANT,

    PIZZA_MENUS_SNACKS,
    AUTRES_PLATS_CUISINES,
    CONVENIENCE,

    PLATS_CUISINES,
    GLACE_ENTREMETS,
    POISSON_FRUITS_DE_MER,
    APERITIF_DESSERT,
    LEGUMES_POMMES_DE_TERRE,
    FRUITS_SURGELES,
    VIANDE,
    PIZZA,
    BOULANGERIE_PATISSERIE_SURGELES,

    BARRES_DE_CEREALES,
    CEREALES,
    MUEESLI,

    FROMAGES,
    YOGOURT,
    LAIT_BOISSONS_LACTEES,
    BIRCHERMUEESLI,
    CREME,
    DESSERT,
    SERE,
    BEURRE,
    MARGARINE,
    DIVERS_PRODUITS_LAITIERS,
    OEUFS,

    TRAITEUR,
    FRUITS_DE_MER_CREVETTES,
    POISSON_FRAIS,

    SNACKS,
    BISCUITS_APERITIFS,
    CHIPS,
    NOIX_GRILLEES,
    POPCORN,

    UNDETERMINED;

    private static BiMap<ClusterTypeSecondLevel,String> stringMap = getStringMap();

    private static BiMap<ClusterTypeSecondLevel,String> getStringMap()
    {
        HashBiMap<ClusterTypeSecondLevel,String> bimap = HashBiMap.create();

        for(ClusterTypeSecondLevel type: values())
        {
            bimap.put(type, type.name());
        }

        bimap.put(BONBONS_CHEWING_GUM, "Bonbons et Chewing-Gums");
        bimap.put(CHOCOLAT, "Chocolat");
        bimap.put(SAIN_BIO, "Sain, BIO");
        bimap.put(BISCUITS_GAUFRES, "Biscuits, Gaufres");
        bimap.put(DIVERS_CHOCOLAT, "Divers Chocolats");
        bimap.put(SPECIALITES, "Spécialités");
        bimap.put(BLEVITA, "Blévita");
        bimap.put(JOURS_DE_FETES_EVENEMENTS, "Jours de Fetes, Evenements");
        bimap.put(BISCUITS_POUR_ENFANTS, "Biscuits pour Enfants");
        bimap.put(BISCUITS_POUR_ALLERGIQUES, "Biscuits pour personnes allergiques");
        bimap.put(DIVERS, "Biscuits divers");
        bimap.put(MIEL, "Miel");
        bimap.put(CONFITURES, "Confitures");
        bimap.put(CACAO_CHOCOLATS_EN_POUDRE, "Cacao, Chocolats en poudre");
        bimap.put(PATES_A_TARTINER, "Pâtes à tartiner");
        bimap.put(PATES_A_TARTINER, "Pâtes à tartiner");
        bimap.put(THE, "Boissons énergétiques");
        bimap.put(CAFE, "Café");
        bimap.put(SIROP_SODA, "Sirops, Sodas");

        return bimap;

    }

    private static Map<ClusterTypeSecondLevel, Integer> imageIdMap = getImageIdMap();

    private static Map<ClusterTypeSecondLevel,Integer> getImageIdMap()
    {
        Map<ClusterTypeSecondLevel, Integer> map = new HashMap<>();
        map.put(FROMAGES, R.drawable.cheese);

        return map;
    }

    public String toString()
    {
        return stringMap.get(this);
    }

    public static ClusterTypeSecondLevel getClusterType(String name)
    {
        if (stringMap.inverse().containsKey(name)) {
            return stringMap.inverse().get(name);
        }
        return UNDETERMINED;
    }

    public ClusterTypeFirstLevel getParent()
    {
        switch(this) {
            case BONBONS_CHEWING_GUM:
            case CHOCOLAT:
            case SAIN_BIO:
            case BISCUITS_GAUFRES:
            case DIVERS_CHOCOLAT:
            case SPECIALITES:
            case BLEVITA:
            case JOURS_DE_FETES_EVENEMENTS:
            case BISCUITS_POUR_ENFANTS:
            case BISCUITS_POUR_ALLERGIQUES:
            case DIVERS:
                return ClusterTypeFirstLevel.CHOCOLAT;

            case CACAO_CHOCOLATS_EN_POUDRE:
            case CONFITURES_PORTIONS_DE_MIEL:
            case CONFITURES:
            case MIEL:
            case PATES_A_TARTINER:
                return ClusterTypeFirstLevel.PETIT_DEJEUNER;

            case BOISSONS_ENERGETIQUES:
            case THE:
            case SIROP_SODA:
            case CAFE:
            case THE_GLACE:
            case BOISSONS_SUCREES:
            case JUS_DE_FRUITS_DE_LEGUMES:
            case EAU_AROMATISEE:
            case BOISSONS_DAPERITIF:
            case EAU_MINERALE:
            case BOISSONS_FAIBLES_EN_CALORIES:
                return ClusterTypeFirstLevel.BOISSONS_CHAUDES_FROIDES;

            case TOMATES_EN_CONSERVE:
            case DU_MONDE_ENTIER:
            case SOUPES_SAUCES_BOUILLON:
            case PATES:
            case LEGUMES_A_SALADE:
            case LEGUMES_LEGUMES_AU_VINAIGRE:
            case EPICES_HERBES:
            case GARNITURES_MOULUES_CEREALES:
            case CHAMPIGNONS_OLIVES_EN_CONSERVE:
            case ANTIPASTI:
            case VIANDE_EN_CONSERVE:
            case POMMES_DE_TERRE_SPAETZLI:
            case INGREDIENTS_DE_CUISSON:
            case POISSON_EN_CONSERVE:
            case GARNITURES_BIO:
            case CHAMPIGNONS:
            case RIZ:
            case FRUITS_EN_CONSERVE:
            case LEGUMINEUSES_HARICOTS_VERTS_SECHES:
            case LEGUMINEUSES_FARCE_POUR_VOL_AU_VENT:
            case DIVERS_GARNITURES:
                return ClusterTypeFirstLevel.GARNITURES_INGREDIENTS;

            case LEGUMES:
            case FRUITS:
                return ClusterTypeFirstLevel.FRUITS_ET_LEGUMES;

            case VOLAILLE:
            case SAUCISSES:
            case VIANDE_SECHEE_JAMBON:
            case SALAMI_LARD:
            case PRODUITS_DE_CHARCUTERIE:
            case PORC:
            case BOEUF:
            case LAPIN:
            case VEAU:
            case AGNEAU_CHEVRE:
            case GIBIER:
            case BIO:
            case SOUS_PRODUITS:
            case ACCOMPAGNEMENTS:
            case CHEVAL:
            case DIVERS_VIANDES:
                return ClusterTypeFirstLevel.VIANDE;

            case FARINE:
            case PRODUITS_FRAIS_DE_BOULANGERIE_NON_REFRIGERES:
            case INGREDIENTS_DE_BOULANGERIE:
            case BOULANGERIE_PATISSERIE:
            case PRODUITS_FRAIS_DE_BOULANGERIE_REFRIGERES:
            case PETITS_PAINS:
            case PAINS_DE_MIE:
            case PAINS:
            case PRODUITS_SANS_GLUTEN:
            case PAINS_LONGUE_CONSERVATION:
            case PAINS_BIO_LONGUE_CONSERVATION:
            case FARINES_SANS_GLUTEN:
            case BISCOTTES_PAIN_CROUSTILLANT:
                return ClusterTypeFirstLevel.BOULANGERIE;

            case PIZZA_MENUS_SNACKS:
            case AUTRES_PLATS_CUISINES:
            case CONVENIENCE:
                return ClusterTypeFirstLevel.PLATS_CUISINES;

            case PLATS_CUISINES:
            case GLACE_ENTREMETS:
            case POISSON_FRUITS_DE_MER:
            case APERITIF_DESSERT:
            case LEGUMES_POMMES_DE_TERRE:
            case FRUITS_SURGELES:
            case VIANDE:
            case PIZZA:
            case BOULANGERIE_PATISSERIE_SURGELES:
                return ClusterTypeFirstLevel.PRODUITS_SURGELES;

            case BARRES_DE_CEREALES:
            case CEREALES:
            case MUEESLI:
                return ClusterTypeFirstLevel.MUEESLI_CEREALES;

            case FROMAGES:
            case YOGOURT:
            case LAIT_BOISSONS_LACTEES:
            case BIRCHERMUEESLI:
            case CREME:
            case DESSERT:
            case SERE:
            case BEURRE:
            case MARGARINE:
            case DIVERS_PRODUITS_LAITIERS:
            case OEUFS:
                return ClusterTypeFirstLevel.PRODUITS_LAITIERS_OEUFS;

            case TRAITEUR:
            case FRUITS_DE_MER_CREVETTES:
            case POISSON_FRAIS:
                return ClusterTypeFirstLevel.POISSON_FRUITS_DE_MER;

            case SNACKS:
            case BISCUITS_APERITIFS:
            case CHIPS:
            case NOIX_GRILLEES:
            case POPCORN:
                return ClusterTypeFirstLevel.APERITIF;

            default:
                return ClusterTypeFirstLevel.UNDETERMINED;

        }
    }

    public ClusterType[] getChildren()
    {
        return null;
    }

    @Override
    public Integer getImageId()
    {
        if(imageIdMap.containsKey(this))
        {
            return imageIdMap.get(this);
        }
        return getParent().getImageId();
    }

}