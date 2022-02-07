package com.example.pyramid.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class _BaseService {
    @PersistenceContext
    EntityManager em;
}
