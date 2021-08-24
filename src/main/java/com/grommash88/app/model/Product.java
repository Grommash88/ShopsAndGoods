package com.grommash88.app.model;

import lombok.*;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Product {

  private ObjectId id;

  private String name;

  private double price;
}
