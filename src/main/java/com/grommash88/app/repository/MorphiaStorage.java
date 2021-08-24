package com.grommash88.app.repository;

/*

import com.lakeev.shopsAndGoods.messages.Messages;
import com.lakeev.shopsAndGoods.model.Market;
import com.lakeev.shopsAndGoods.model.Product;
import com.lakeev.shopsAndGoods.model.Response;
import com.lakeev.shopsAndGoods.repository.MorphiaRepository;

import com.mongodb.client.model.UnwindOptions;
import dev.morphia.Datastore;
import dev.morphia.aggregation.Accumulator;
import dev.morphia.aggregation.Group;
import dev.morphia.aggregation.Projection;
import dev.morphia.query.Query;
import dev.morphia.aggregation.Sort;
import dev.morphia.query.UpdateOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MorphiaStorage implements Storage, AutoCloseable {

    private final int ONE = 1;
    private final int ZERO = 0;

    private final MorphiaRepository repository;
    private final Datastore datastore;
    private final Messages messages;

    public MorphiaStorage() {

        repository = MorphiaRepository.getInstance();
        datastore = repository.getDatastore();
        datastore.ensureIndexes();
        messages = Messages.INSTANCE;
    }

    @Override
    public void addMarket(String marketName) {

        if (countOfMarketsWithThatName(marketName) == ZERO) {

            Market market = Market.builder()
                    .name(marketName)
                    .build();
            datastore.save(market);
        } else {

            try {

                throw new IllegalArgumentException(String.format(messages.getMARKET_EXIST_MSG(), marketName));
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }

    }

    @Override
    public void deleteMarket(String marketName) {

        try {

            if (countOfMarketsWithThatName(marketName) == ONE) {

                datastore.delete(datastore.createQuery(Market.class).field("name").contains(marketName));
            } else if (countOfMarketsWithThatName(marketName) == ZERO) {

                throw new IllegalArgumentException(String.format(messages.getMARKET_NO_EXIST_MSG(), marketName));
            } else {

                throw new IllegalArgumentException(String.format(messages.getFOUND_MORE_MARKETS_MSG(), ONE, marketName));
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void addProduct(String productDescription) {

        String productName = productDescription.substring(ZERO, productDescription.lastIndexOf(" ")).trim();
        int productPrice = Integer.parseInt(productDescription.substring(productDescription
                .lastIndexOf(" ")).trim());

        if (countOfProductsWithThatName(productName) == ZERO) {

            Product product = Product.builder()
                    .name(productName)
                    .price(productPrice)
                    .build();
            datastore.save(product);
        } else {

            try {

                throw new IllegalArgumentException(String.format(messages.getPRODUCT_EXIST_MSG(), productName));
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteProduct(String productName) {

        try {

            if (countOfMarketsWithThatName(productName) == ONE) {

                datastore.delete(datastore.createQuery(Product.class).field("name").contains(productName));
            } else if (countOfMarketsWithThatName(productName) == ZERO) {

                throw new IllegalArgumentException(String.format(messages.getPRODUCT_NO_EXIST_MSG(), productName));
            } else {

                throw new IllegalArgumentException(String
                        .format(messages.getFOUND_MORE_PRODUCTS_MSG(), ONE, productName)
                );
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void putProductUpForSaleInAMarket(String description) {

        String[] data = description.split("\\s");
        String marketName = data[ONE].trim();
        String productName = data[ZERO].trim();

        try {

            Product product = findProductByName(productName).first();
            UpdateOperations<Market> updateOperations =
                    datastore.createUpdateOperations(Market.class).addToSet("products", product);
            datastore.update(findMarketByName(marketName), updateOperations);
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void showStatisticsOfMarketProducts() {

    }

    @Override
    public void showStatisticsOfMarketProducts(String marketName) {

        System.out.println(String.format(messages.getPRODUCT_STATISTICS_BY_MARKET_MSG(), marketName));

        try {
            if (findMarketByName(marketName).field("products").exists().count() == ONE) {

                showCountOfProductsInTheMarket(getCountOfProductsInTheMarket(marketName));
                showTheAveragePriceOfProductsInTheMarket(getTheAveragePriceOfProductsInTheMarket(marketName));
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(messages.getSPECIFY_TARGET_PRICE_MSG());
                int targetPrice = Integer.parseInt(reader.readLine().trim());
                showTheCountOfProductsInTheMarketCheaperThan(getTheCountOfProductsInTheMarketCheaperThan(
                        marketName, targetPrice), targetPrice);
                showTheMostExpensiveAndCheapestProductsInTheMarket(marketName);

            } else if (findMarketByName(marketName).field("products").exists().count() > ONE) {

                throw new IllegalArgumentException(String.format(messages.getFOUND_MORE_MARKETS_MSG(), ONE, marketName));

            } else {

                throw new IllegalArgumentException(String.format(messages.getNO_PRODUCTS_FOR_SALE_MSG(), marketName));
            }
        } catch (IllegalArgumentException | IOException e) {

            e.printStackTrace();
        }
    }



    @Override
    public void showStatisticsOfAllMarkets() {
        datastore.createAggregation(Market.class)
                //.unwind("products", new UnwindOptions().preserveNullAndEmptyArrays(true))
                .match(datastore.createQuery(Market.class).field("products").exists())
                //.aggregate(Market.class).forEachRemaining(System.out::println);
                .group(Group.grouping("_id", "name"))
                //.unwind("products")
                .project(Projection.expression("response",
                        Projection.projection("$size", "products")))
                .aggregate(Response.class).forEachRemaining(System.out::println);
    }

    private void showCountOfProductsInTheMarket(Iterator<Response> responseIterator) {

        responseIterator.forEachRemaining(response -> System.out.println(String
                .format(messages.getCOUNT_OF_PRODUCTS_IN_THE_MARKET_MSG(), response.getResponse()))
        );
    }

    private Iterator<Response> getCountOfProductsInTheMarket(String marketName) {

        return datastore.createAggregation(Market.class)
                .match(findMarketByName(marketName))
                .project(Projection.expression("response",
                        Projection.projection("$size", "products")))
                .aggregate(Response.class);
    }

    private void showTheAveragePriceOfProductsInTheMarket(Iterator<Response> responseIterator) {

        responseIterator.forEachRemaining(response ->
                System.out.println(String.format(messages.getAVERAGE_PRICE_OF_A_PRODUCTS_IN_THE_MARKET_MSG(),
                        response.getResponse())));
    }

    private Iterator<Response> getTheAveragePriceOfProductsInTheMarket(String marketName) {

        return datastore.createAggregation(Market.class)
                .match(findMarketByName(marketName))
                .unwind("products")
                .group(Group.grouping("response", Accumulator
                        .accumulator("$avg", "products.price")))
                .aggregate(Response.class);
    }

    private void showTheCountOfProductsInTheMarketCheaperThan(Iterator<Response> responseIterator, int targetPrice) {

        if (responseIterator.hasNext()) {

            responseIterator.forEachRemaining(response ->
                    System.out.println(String.format(messages.getTHE_COUNT_OF_PRODUCTS_BELOW_THE_TARGET_PRICE_MSG(),
                            targetPrice, response.getResponse()))
            );
        } else {

            System.out.println(String
                    .format(messages.getTHE_COUNT_OF_PRODUCTS_BELOW_THE_TARGET_PRICE_MSG(), targetPrice, ZERO));
        }
    }

    private Iterator<Response> getTheCountOfProductsInTheMarketCheaperThan(String marketName, int targetPrice) {
        return datastore.createAggregation(Market.class)
                .match(findMarketByName(marketName))
                .unwind("products")
                .match(datastore.createQuery(Product.class)
                        .disableValidation()
                        .field("products.price").lessThan(targetPrice))
                .group(Group.grouping("response", Accumulator
                        .accumulator("$sum", ONE)))
                .aggregate(Response.class);
    }

    private void showTheMostExpensiveAndCheapestProductsInTheMarket(String marketName) {

        Product cheapestProduct = getTheCheapestProductInTheMarket(marketName);
        Product expensiveProduct = getTheMostExpensiveProductInTheMarket(marketName);
        System.out.println(String.format(messages.getTHE_MOST_EXPENSIVE_AND_CHEAPEST_PRODUCTS_IN_THE_MARKET_MSG(),
                expensiveProduct.getName(), expensiveProduct.getPrice(),
                cheapestProduct.getName(), cheapestProduct.getPrice())
        );
    }

    private Product getTheMostExpensiveProductInTheMarket(String marketName) {

        return findProductByName(datastore.createAggregation(Market.class)
                .match(findMarketByName(marketName))
                .unwind("products")
                .sort(new Sort("products.price", ONE))
                .project(Projection.projection("_id").suppress())
                .project(Projection.projection("name").suppress())
                .group(Group.grouping("response", Accumulator.
                        accumulator("$last", "products.name")))
                .aggregate(Response.class).next().getResponse()).first();
    }

    private Product getTheCheapestProductInTheMarket(String marketName) {

        return findProductByName(datastore.createAggregation(Market.class)
                .match(findMarketByName(marketName))
                .unwind("products")
                .sort(new Sort("products.price", ONE))
                .project(Projection.projection("_id").suppress())
                .project(Projection.projection("name").suppress())
                .group(Group.grouping("response", Accumulator.
                        accumulator("$first", "products.name")))
                .aggregate(Response.class).next().getResponse()).first();
    }

    private long countOfProductsWithThatName(String name) {

        return datastore.find(Product.class)
                .filter("name", name)
                .count();
    }

    private long countOfMarketsWithThatName(String name) {

        return datastore.find(Market.class)
                .filter("name", name)
                .count();
    }

    private Query<Market> findMarketByName(String marketName) {

        return Optional.ofNullable(datastore.find(Market.class).disableValidation().filter("name", marketName))
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format(messages.getMARKET_NO_EXIST_MSG(), marketName))
                );
    }

    private Query<Product> findProductByName(String productName) {

        return Optional.ofNullable(datastore.find(Product.class).filter("name", productName))
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format(messages.getPRODUCT_NO_EXIST_MSG(), productName))
                );
    }

    @Override
    public void close() throws Exception {

        repository.close();
        System.out.println(messages.getEXIT_MSG());
    }
}


 */
