package ch.epfl.sweng.qeeqbii.clustering;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.R;

/**
 * Created by guillaume on 23/11/17.
 * First Level of clustering
 */


public enum ClusterTypeFirstLevel implements ClusterType, Serializable {
    CHOCOLAT,
    PETIT_DEJEUNER,
    BOISSONS_CHAUDES_FROIDES,
    GARNITURES_INGREDIENTS,
    FRUITS_ET_LEGUMES,
    VIANDE,
    BOULANGERIE,
    PLATS_CUISINES,
    PRODUITS_SURGELES,
    MUEESLI_CEREALES,
    PRODUITS_LAITIERS_OEUFS,
    POISSON_FRUITS_DE_MER,
    APERITIF,
    UNDETERMINED;

    private static Map<ClusterTypeFirstLevel,Integer> imageIdMap = createImageIdMap();

    private static Map<ClusterTypeFirstLevel,Integer> createImageIdMap()
    {
        Map<ClusterTypeFirstLevel, Integer> map = new HashMap<>();
        map.put(CHOCOLAT, R.drawable.buiscuit);
        map.put(PETIT_DEJEUNER, R.drawable.buiscuit);
        map.put(BOISSONS_CHAUDES_FROIDES, R.drawable.bottle);
        map.put(GARNITURES_INGREDIENTS, R.drawable.carot);
        map.put(FRUITS_ET_LEGUMES, R.drawable.vegetables);
        map.put(VIANDE, R.drawable.meat);
        map.put(BOULANGERIE, R.drawable.boulangerie);
        map.put(PLATS_CUISINES, R.drawable.saussage);
        map.put(PRODUITS_SURGELES, R.drawable.cocktail);
        map.put(MUEESLI_CEREALES, R.drawable.donut);
        map.put(PRODUITS_LAITIERS_OEUFS, R.drawable.bottle_1);
        map.put(POISSON_FRUITS_DE_MER, R.drawable.fruit);
        map.put(APERITIF, R.drawable.cocktail);
        map.put(UNDETERMINED, R.drawable.mug);

        return map;
    }

    private static BiMap<ClusterTypeFirstLevel,String> stringMap = createStringMap();

    public static ClusterTypeFirstLevel getClusterType(String name)
    {
        if (stringMap.inverse().containsKey(name)) {
            return stringMap.inverse().get(name);
        }
        return UNDETERMINED;
    }

    private static BiMap<ClusterTypeFirstLevel,String> createStringMap()
    {
        HashBiMap<ClusterTypeFirstLevel,String> bimap = HashBiMap.create();

        bimap.put(CHOCOLAT, "Chocolat");
        bimap.put(PETIT_DEJEUNER, "Petit Déjeuner");
        bimap.put(BOISSONS_CHAUDES_FROIDES, "Boissons chaudes ou froides");
        bimap.put(GARNITURES_INGREDIENTS, "Garnitures, Ingrédients");
        bimap.put(FRUITS_ET_LEGUMES, "Fruits et légumes");
        bimap.put(VIANDE, "Viande");
        bimap.put(BOULANGERIE, "Boulangerie");
        bimap.put(PLATS_CUISINES, "Plats cuisinés");
        bimap.put(PRODUITS_SURGELES, "Produits Surgelés");
        bimap.put(MUEESLI_CEREALES, "Mueesli, Céréales");
        bimap.put(PRODUITS_LAITIERS_OEUFS, "Produits laitiers, Oeufs");
        bimap.put(POISSON_FRUITS_DE_MER, "Poisson, Fruits de Mer");
        bimap.put(APERITIF, "Apéritif");
        bimap.put(UNDETERMINED, "Undetermined");

        return bimap;
    }

    @Override
    public String toString()
    {
        return stringMap.get(this);
    }

    @Override
    public ClusterTypeSecondLevel[] getChildren()
    {
        switch(this)
        {
            case CHOCOLAT:
                return new ClusterTypeSecondLevel[]{ ClusterTypeSecondLevel.BONBONS_CHEWING_GUM,
                        ClusterTypeSecondLevel.CHOCOLAT,
                        ClusterTypeSecondLevel.SAIN_BIO,
                        ClusterTypeSecondLevel.BISCUITS_GAUFRES,
                        ClusterTypeSecondLevel.DIVERS_CHOCOLAT,
                        ClusterTypeSecondLevel.SPECIALITES,
                        ClusterTypeSecondLevel.BLEVITA,
                        ClusterTypeSecondLevel.JOURS_DE_FETES_EVENEMENTS,
                        ClusterTypeSecondLevel.BISCUITS_POUR_ENFANTS,
                        ClusterTypeSecondLevel.BISCUITS_POUR_ALLERGIQUES,
                        ClusterTypeSecondLevel.DIVERS};



            case PETIT_DEJEUNER:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.CACAO_CHOCOLATS_EN_POUDRE,
                        ClusterTypeSecondLevel.CONFITURES_PORTIONS_DE_MIEL,
                        ClusterTypeSecondLevel.CONFITURES,
                        ClusterTypeSecondLevel.MIEL,
                        ClusterTypeSecondLevel.PATES_A_TARTINER};

            case BOISSONS_CHAUDES_FROIDES:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.BOISSONS_ENERGETIQUES,
                        ClusterTypeSecondLevel.THE,
                        ClusterTypeSecondLevel.SIROP_SODA,
                        ClusterTypeSecondLevel.CAFE,
                        ClusterTypeSecondLevel.THE_GLACE,
                        ClusterTypeSecondLevel.BOISSONS_SUCREES,
                        ClusterTypeSecondLevel.JUS_DE_FRUITS_DE_LEGUMES,
                        ClusterTypeSecondLevel.EAU_AROMATISEE,
                        ClusterTypeSecondLevel.BOISSONS_DAPERITIF,
                        ClusterTypeSecondLevel.EAU_MINERALE,
                        ClusterTypeSecondLevel.BOISSONS_FAIBLES_EN_CALORIES};

            case GARNITURES_INGREDIENTS:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.TOMATES_EN_CONSERVE,
                        ClusterTypeSecondLevel.DU_MONDE_ENTIER,
                        ClusterTypeSecondLevel.SOUPES_SAUCES_BOUILLON,
                        ClusterTypeSecondLevel.PATES,
                        ClusterTypeSecondLevel.LEGUMES_A_SALADE,
                        ClusterTypeSecondLevel.LEGUMES_LEGUMES_AU_VINAIGRE,
                        ClusterTypeSecondLevel.EPICES_HERBES,
                        ClusterTypeSecondLevel.GARNITURES_MOULUES_CEREALES,
                        ClusterTypeSecondLevel.CHAMPIGNONS_OLIVES_EN_CONSERVE,
                        ClusterTypeSecondLevel.ANTIPASTI,
                        ClusterTypeSecondLevel.VIANDE_EN_CONSERVE,
                        ClusterTypeSecondLevel.POMMES_DE_TERRE_SPAETZLI,
                        ClusterTypeSecondLevel.INGREDIENTS_DE_CUISSON,
                        ClusterTypeSecondLevel.POISSON_EN_CONSERVE,
                        ClusterTypeSecondLevel.GARNITURES_BIO,
                        ClusterTypeSecondLevel.CHAMPIGNONS,
                        ClusterTypeSecondLevel.RIZ,
                        ClusterTypeSecondLevel.FRUITS_EN_CONSERVE,
                        ClusterTypeSecondLevel.LEGUMINEUSES_HARICOTS_VERTS_SECHES,
                        ClusterTypeSecondLevel.LEGUMINEUSES_FARCE_POUR_VOL_AU_VENT,
                        ClusterTypeSecondLevel.DIVERS_GARNITURES};

            case FRUITS_ET_LEGUMES:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.LEGUMES,
                        ClusterTypeSecondLevel.FRUITS};

            case VIANDE:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.VOLAILLE,
                        ClusterTypeSecondLevel.SAUCISSES,
                        ClusterTypeSecondLevel.VIANDE_SECHEE_JAMBON,
                        ClusterTypeSecondLevel.SALAMI_LARD,
                        ClusterTypeSecondLevel.PRODUITS_DE_CHARCUTERIE,
                        ClusterTypeSecondLevel.PORC,
                        ClusterTypeSecondLevel.BOEUF,
                        ClusterTypeSecondLevel.LAPIN,
                        ClusterTypeSecondLevel.VEAU,
                        ClusterTypeSecondLevel.AGNEAU_CHEVRE,
                        ClusterTypeSecondLevel.GIBIER,
                        ClusterTypeSecondLevel.BIO,
                        ClusterTypeSecondLevel.SOUS_PRODUITS,
                        ClusterTypeSecondLevel.ACCOMPAGNEMENTS,
                        ClusterTypeSecondLevel.CHEVAL,
                        ClusterTypeSecondLevel.DIVERS_VIANDES};

            case BOULANGERIE:
                return  new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.FARINE,
                        ClusterTypeSecondLevel.PRODUITS_FRAIS_DE_BOULANGERIE_NON_REFRIGERES,
                        ClusterTypeSecondLevel.INGREDIENTS_DE_BOULANGERIE,
                        ClusterTypeSecondLevel.BOULANGERIE_PATISSERIE,
                        ClusterTypeSecondLevel.PRODUITS_FRAIS_DE_BOULANGERIE_REFRIGERES,
                        ClusterTypeSecondLevel.PETITS_PAINS,
                        ClusterTypeSecondLevel.PAINS_DE_MIE,
                        ClusterTypeSecondLevel.PAINS,
                        ClusterTypeSecondLevel.PRODUITS_SANS_GLUTEN,
                        ClusterTypeSecondLevel.PAINS_LONGUE_CONSERVATION,
                        ClusterTypeSecondLevel.PAINS_BIO_LONGUE_CONSERVATION,
                        ClusterTypeSecondLevel.FARINES_SANS_GLUTEN,
                        ClusterTypeSecondLevel.BISCOTTES_PAIN_CROUSTILLANT
                };

            case PLATS_CUISINES:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.PIZZA_MENUS_SNACKS,
                        ClusterTypeSecondLevel.AUTRES_PLATS_CUISINES,
                        ClusterTypeSecondLevel.CONVENIENCE};

            case PRODUITS_SURGELES:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.PLATS_CUISINES,
                        ClusterTypeSecondLevel.GLACE_ENTREMETS,
                        ClusterTypeSecondLevel.POISSON_FRUITS_DE_MER,
                        ClusterTypeSecondLevel.APERITIF_DESSERT,
                        ClusterTypeSecondLevel.LEGUMES_POMMES_DE_TERRE,
                        ClusterTypeSecondLevel.FRUITS_SURGELES,
                        ClusterTypeSecondLevel.VIANDE,
                        ClusterTypeSecondLevel.PIZZA,
                        ClusterTypeSecondLevel.BOULANGERIE_PATISSERIE_SURGELES};

            case MUEESLI_CEREALES:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.BARRES_DE_CEREALES,
                        ClusterTypeSecondLevel.CEREALES,
                        ClusterTypeSecondLevel.MUEESLI};

            case PRODUITS_LAITIERS_OEUFS:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.FROMAGES,
                        ClusterTypeSecondLevel.YOGOURT,
                        ClusterTypeSecondLevel.LAIT_BOISSONS_LACTEES,
                        ClusterTypeSecondLevel.BIRCHERMUEESLI,
                        ClusterTypeSecondLevel.CREME,
                        ClusterTypeSecondLevel.DESSERT,
                        ClusterTypeSecondLevel.SERE,
                        ClusterTypeSecondLevel.BEURRE,
                        ClusterTypeSecondLevel.MARGARINE,
                        ClusterTypeSecondLevel.DIVERS_PRODUITS_LAITIERS,
                        ClusterTypeSecondLevel.OEUFS};

            case POISSON_FRUITS_DE_MER:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.TRAITEUR,
                        ClusterTypeSecondLevel.FRUITS_DE_MER_CREVETTES,
                        ClusterTypeSecondLevel.POISSON_FRAIS};

            case APERITIF:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.SNACKS,
                        ClusterTypeSecondLevel.BISCUITS_APERITIFS,
                        ClusterTypeSecondLevel.CHIPS,
                        ClusterTypeSecondLevel.NOIX_GRILLEES,
                        ClusterTypeSecondLevel.POPCORN};

            default:
                return new ClusterTypeSecondLevel[]{ClusterTypeSecondLevel.UNDETERMINED};
        }
    }

    @Override
    public ClusterType getParent()
    {
        return null;
    }

    @Override
    public Integer getImageId()
    {
        return imageIdMap.get(this);
    }


}