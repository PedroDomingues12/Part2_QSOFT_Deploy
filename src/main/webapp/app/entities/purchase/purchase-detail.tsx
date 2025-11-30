import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchase.reducer';

export const PurchaseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const purchaseEntity = useAppSelector(state => state.purchase.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchaseDetailsHeading">
          <Translate contentKey="smartdineApp.purchase.detail.title">Purchase</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchaseEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="smartdineApp.purchase.date">Date</Translate>
            </span>
          </dt>
          <dd>{purchaseEntity.date ? <TextFormat value={purchaseEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="ammount">
              <Translate contentKey="smartdineApp.purchase.ammount">Ammount</Translate>
            </span>
          </dt>
          <dd>{purchaseEntity.ammount}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartdineApp.purchase.status">Status</Translate>
            </span>
          </dt>
          <dd>{purchaseEntity.status}</dd>
          <dt>
            <span id="paymentMethod">
              <Translate contentKey="smartdineApp.purchase.paymentMethod">Payment Method</Translate>
            </span>
          </dt>
          <dd>{purchaseEntity.paymentMethod}</dd>
          <dt>
            <Translate contentKey="smartdineApp.purchase.user">User</Translate>
          </dt>
          <dd>{purchaseEntity.user ? purchaseEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="smartdineApp.purchase.dish">Dish</Translate>
          </dt>
          <dd>
            {purchaseEntity.dishes
              ? purchaseEntity.dishes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {purchaseEntity.dishes && i === purchaseEntity.dishes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/purchase" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchase/${purchaseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchaseDetail;
