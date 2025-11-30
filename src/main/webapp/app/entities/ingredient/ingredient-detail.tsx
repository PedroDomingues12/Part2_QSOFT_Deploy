import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingredient.reducer';

export const IngredientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingredientEntity = useAppSelector(state => state.ingredient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientDetailsHeading">
          <Translate contentKey="smartdineApp.ingredient.detail.title">Ingredient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartdineApp.ingredient.name">Name</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.name}</dd>
          <dt>
            <span id="enabled">
              <Translate contentKey="smartdineApp.ingredient.enabled">Enabled</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="smartdineApp.ingredient.category">Category</Translate>
            </span>
          </dt>
          <dd>{ingredientEntity.category}</dd>
        </dl>
        <Button tag={Link} to="/ingredient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingredient/${ingredientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngredientDetail;
