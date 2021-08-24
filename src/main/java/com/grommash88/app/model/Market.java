package com.grommash88.app.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Market {

  private ObjectId id;

  private String name;

  private Set<Product> products;
}
