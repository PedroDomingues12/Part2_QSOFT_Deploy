import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu.reducer';

export const MenuDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuEntity = useAppSelector(state => state.menu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuDetailsHeading">
          <Translate contentKey="smartdineApp.menu.detail.title">Menu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartdineApp.menu.name">Name</Translate>
            </span>
          </dt>
          <dd>{menuEntity.name}</dd>
          <dt>
            <Translate contentKey="smartdineApp.menu.dishNames">Dish Names</Translate>
          </dt>
          <dd>{menuEntity.dishNames ? menuEntity.dishNames.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/menu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu/${menuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuDetail;
