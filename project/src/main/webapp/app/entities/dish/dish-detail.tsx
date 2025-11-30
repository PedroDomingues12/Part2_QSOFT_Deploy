import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dish.reducer';

export const DishDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dishEntity = useAppSelector(state => state.dish.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dishDetailsHeading">
          <Translate contentKey="smartdineApp.dish.detail.title">Dish</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dishEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartdineApp.dish.name">Name</Translate>
            </span>
          </dt>
          <dd>{dishEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="smartdineApp.dish.price">Price</Translate>
            </span>
          </dt>
          <dd>{dishEntity.price}</dd>
          <dt>
            <Translate contentKey="smartdineApp.dish.ingridientName">Ingridient Name</Translate>
          </dt>
          <dd>{dishEntity.ingridientName ? dishEntity.ingridientName.name : ''}</dd>
          <dt>
            <Translate contentKey="smartdineApp.dish.purchase">Purchase</Translate>
          </dt>
          <dd>
            {dishEntity.purchases
              ? dishEntity.purchases.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {dishEntity.purchases && i === dishEntity.purchases.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/dish" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dish/${dishEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DishDetail;
