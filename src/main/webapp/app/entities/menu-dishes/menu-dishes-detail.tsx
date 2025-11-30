import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu-dishes.reducer';

export const MenuDishesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuDishesEntity = useAppSelector(state => state.menuDishes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuDishesDetailsHeading">
          <Translate contentKey="smartdineApp.menuDishes.detail.title">MenuDishes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuDishesEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartdineApp.menuDishes.name">Name</Translate>
            </span>
          </dt>
          <dd>{menuDishesEntity.name}</dd>
          <dt>
            <Translate contentKey="smartdineApp.menuDishes.dishNames">Dish Names</Translate>
          </dt>
          <dd>{menuDishesEntity.dishNames ? menuDishesEntity.dishNames.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/menu-dishes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu-dishes/${menuDishesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuDishesDetail;
